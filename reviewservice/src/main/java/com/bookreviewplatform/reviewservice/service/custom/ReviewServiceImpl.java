package com.bookreviewplatform.reviewservice.service.custom;

import com.bookreviewplatform.reviewservice.dto.BookDTO;
import com.bookreviewplatform.reviewservice.dto.ReviewDTO;
import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.dto.UserDTO;
import com.bookreviewplatform.reviewservice.entity.Review;
import com.bookreviewplatform.reviewservice.payloads.StandardResponse;
import com.bookreviewplatform.reviewservice.repository.ReviewRepository;
import com.bookreviewplatform.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final Logger logger = Logger.getLogger(ReviewServiceImpl.class.getName());
    private final ReviewRepository reviewRepository;
    private final WebClient bookWebClient;
    private final WebClient userWebClient;

    @Override
    public List<ReviewDTO> getAllReviews() {
        logger.fine("Fetching all reviews from database");
        List<ReviewDTO> reviews = reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.fine("Found " + reviews.size() + " reviews in database");
        return reviews;
    }

    @Override
    public ReviewDTO getReviewById(UUID id) {
        logger.fine("Searching for review with id: " + id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    logger.severe("Review not found with id: " + id);
                    return new RuntimeException("Review not found with id: " + id);
                });
        logger.fine("Review found with id: " + id);
        return convertToDTO(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByBookId(UUID bookId) {
        logger.fine("Fetching reviews for book id: " + bookId);
        List<ReviewDTO> reviews = reviewRepository.findByBookId(bookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.fine("Found " + reviews.size() + " reviews for book id: " + bookId);
        return reviews;
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(UUID userId) {
        logger.fine("Fetching reviews for user id: " + userId);
        List<ReviewDTO> reviews = reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.fine("Found " + reviews.size() + " reviews for user id: " + userId);
        return reviews;
    }

    @Override
    public ReviewDTO saveReview(ReviewRequestDTO reviewRequestDTO) {
        logger.info("Starting review creation process for book id: " + reviewRequestDTO.getBookId() + 
                    " and user id: " + reviewRequestDTO.getUserId());
        
        // Build type refs
        ParameterizedTypeReference<StandardResponse<BookDTO>> bookTypeRef =
                new ParameterizedTypeReference<>() {};
        ParameterizedTypeReference<StandardResponse<UserDTO>> userTypeRef =
                new ParameterizedTypeReference<>() {};

        // Validate book exists
        logger.fine("Calling book service to validate book with id: " + reviewRequestDTO.getBookId());
        StandardResponse<BookDTO> bookResp;
        try {
            bookResp = bookWebClient.get()
                    .uri("/{id}", reviewRequestDTO.getBookId())
                    .retrieve()
                    .bodyToMono(bookTypeRef)
                    .block();
            logger.fine("Successfully received response from book service for book id: " + 
                        reviewRequestDTO.getBookId());
        } catch (WebClientResponseException.NotFound nf) {
            logger.severe("Book not found with id: " + reviewRequestDTO.getBookId());
            throw new RuntimeException("Book not found with id: " + reviewRequestDTO.getBookId());
        } catch (WebClientResponseException e) {
            logger.severe("Book service returned error for book id " + reviewRequestDTO.getBookId() + 
                         ": Status " + e.getStatusCode() + ", Message: " + e.getMessage());
            throw new RuntimeException("Failed to fetch book: " + e.getMessage(), e);
        } catch (WebClientRequestException e) {
            logger.severe("Network error while calling book service for book id " + 
                         reviewRequestDTO.getBookId() + ": " + e.getMessage());
            throw new RuntimeException("Network error while calling book service: " + e.getMessage(), e);
        }

        if (bookResp == null || bookResp.getData() == null) {
            logger.severe("Book service returned null response for book id: " + reviewRequestDTO.getBookId());
            throw new RuntimeException("Book not found with id: " + reviewRequestDTO.getBookId());
        }
        BookDTO bookDTO = bookResp.getData();
        logger.info("Book validated successfully: " + bookDTO.getTitle() + " by " + bookDTO.getAuthor());

        // Validate user exists
        logger.fine("Calling user service to validate user with id: " + reviewRequestDTO.getUserId());
        StandardResponse<UserDTO> userResp;
        try {
            userResp = userWebClient.get()
                    .uri("/{id}", reviewRequestDTO.getUserId())
                    .retrieve()
                    .bodyToMono(userTypeRef)
                    .block();
            logger.fine("Successfully received response from user service for user id: " + 
                        reviewRequestDTO.getUserId());
        } catch (WebClientResponseException.NotFound nf) {
            logger.severe("User not found with id: " + reviewRequestDTO.getUserId());
            throw new RuntimeException("User not found with id: " + reviewRequestDTO.getUserId());
        } catch (WebClientResponseException e) {
            logger.severe("User service returned error for user id " + reviewRequestDTO.getUserId() + 
                         ": Status " + e.getStatusCode() + ", Message: " + e.getMessage());
            throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
        } catch (WebClientRequestException e) {
            logger.severe("Network error while calling user service for user id " + 
                         reviewRequestDTO.getUserId() + ": " + e.getMessage());
            throw new RuntimeException("Network error while calling user service: " + e.getMessage(), e);
        }

        if (userResp == null || userResp.getData() == null) {
            logger.severe("User service returned null response for user id: " + reviewRequestDTO.getUserId());
            throw new RuntimeException("User not found with id: " + reviewRequestDTO.getUserId());
        }
        UserDTO userDTO = userResp.getData();
        logger.info("User validated successfully: " + userDTO.getUsername());

        // Create and save review
        logger.fine("Creating review entity with rating: " + reviewRequestDTO.getRating());
        Review review = Review.builder()
                .rating(reviewRequestDTO.getRating())
                .bookId(reviewRequestDTO.getBookId())
                .userId(reviewRequestDTO.getUserId())
                .build();

        Review savedReview = reviewRepository.save(review);
        logger.info("Review created successfully with id: " + savedReview.getId() + 
                   " for book: " + bookDTO.getTitle() + " by user: " + userDTO.getUsername());
        return convertToDTO(savedReview);
    }

    @Override
    public Boolean deleteReview(UUID id) {
        logger.fine("Checking if review exists with id: " + id);
        if (!reviewRepository.existsById(id)) {
            logger.warning("Review not found with id: " + id);
            return false;
        }
        logger.fine("Deleting review with id: " + id);
        reviewRepository.deleteById(id);
        logger.info("Review deleted successfully with id: " + id);
        return true;
    }


    private ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .bookId(review.getBookId())
                .userId(review.getUserId())
                .build();
    }
}
