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
    public StandardResponse getAllReviews() {
        try {
            logger.fine("Fetching all reviews from database");
            List<ReviewDTO> reviews = reviewRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logger.fine("Found " + reviews.size() + " reviews in database");
            return StandardResponse.success("Reviews retrieved successfully", reviews);
        } catch (Exception e) {
            logger.severe("Error fetching reviews: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve reviews", e.getMessage());
        }
    }

    @Override
    public StandardResponse getReviewById(UUID id) {
        try {
            logger.fine("Searching for review with id: " + id);
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.severe("Review not found with id: " + id);
                        return new RuntimeException("Review not found with id: " + id);
                    });
            logger.fine("Review found with id: " + id);
            return StandardResponse.success("Review retrieved successfully", convertToDTO(review));
        } catch (RuntimeException e) {
            logger.warning("Review not found with id: " + id);
            return StandardResponse.error("Review not found", e.getMessage());
        } catch (Exception e) {
            logger.severe("Error retrieving review: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve review", e.getMessage());
        }
    }

    @Override
    public StandardResponse getReviewsByBookId(UUID bookId) {
        try {
            logger.fine("Fetching reviews for book id: " + bookId);
            List<ReviewDTO> reviews = reviewRepository.findByBookId(bookId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logger.fine("Found " + reviews.size() + " reviews for book id: " + bookId);
            return StandardResponse.success("Reviews retrieved successfully", reviews);
        } catch (Exception e) {
            logger.severe("Error fetching reviews for book: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve reviews", e.getMessage());
        }
    }

    @Override
    public StandardResponse getReviewsByUserId(UUID userId) {
        try {
            logger.fine("Fetching reviews for user id: " + userId);
            List<ReviewDTO> reviews = reviewRepository.findByUserId(userId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logger.fine("Found " + reviews.size() + " reviews for user id: " + userId);
            return StandardResponse.success("Reviews retrieved successfully", reviews);
        } catch (Exception e) {
            logger.severe("Error fetching reviews for user: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve reviews", e.getMessage());
        }
    }

    @Override
    public StandardResponse saveReview(ReviewRequestDTO reviewRequestDTO) {
        try {
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
                return StandardResponse.error("Book not found", "Book with id " + reviewRequestDTO.getBookId() + " does not exist");
            } catch (WebClientResponseException e) {
                logger.severe("Book service returned error for book id " + reviewRequestDTO.getBookId() + 
                             ": Status " + e.getStatusCode() + ", Message: " + e.getMessage());
                return StandardResponse.error("Failed to fetch book", e.getMessage());
            } catch (WebClientRequestException e) {
                logger.severe("Network error while calling book service for book id " + 
                             reviewRequestDTO.getBookId() + ": " + e.getMessage());
                return StandardResponse.error("Network error", "Failed to connect to book service");
            }

            if (bookResp == null || bookResp.getData() == null) {
                logger.severe("Book service returned null response for book id: " + reviewRequestDTO.getBookId());
                return StandardResponse.error("Book not found", "Book with id " + reviewRequestDTO.getBookId() + " does not exist");
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
                return StandardResponse.error("User not found", "User with id " + reviewRequestDTO.getUserId() + " does not exist");
            } catch (WebClientResponseException e) {
                logger.severe("User service returned error for user id " + reviewRequestDTO.getUserId() + 
                             ": Status " + e.getStatusCode() + ", Message: " + e.getMessage());
                return StandardResponse.error("Failed to fetch user", e.getMessage());
            } catch (WebClientRequestException e) {
                logger.severe("Network error while calling user service for user id " + 
                             reviewRequestDTO.getUserId() + ": " + e.getMessage());
                return StandardResponse.error("Network error", "Failed to connect to user service");
            }

            if (userResp == null || userResp.getData() == null) {
                logger.severe("User service returned null response for user id: " + reviewRequestDTO.getUserId());
                return StandardResponse.error("User not found", "User with id " + reviewRequestDTO.getUserId() + " does not exist");
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
            return StandardResponse.success("Review created successfully", convertToDTO(savedReview));
        } catch (Exception e) {
            logger.severe("Error creating review: " + e.getMessage());
            return StandardResponse.error("Failed to create review", e.getMessage());
        }
    }

    @Override
    public StandardResponse deleteReview(UUID id) {
        try {
            logger.fine("Checking if review exists with id: " + id);
            if (!reviewRepository.existsById(id)) {
                logger.warning("Review not found with id: " + id);
                return StandardResponse.error("Review not found", "Review with id " + id + " does not exist");
            }
            logger.fine("Deleting review with id: " + id);
            reviewRepository.deleteById(id);
            logger.info("Review deleted successfully with id: " + id);
            return StandardResponse.success("Review deleted successfully", true);
        } catch (Exception e) {
            logger.severe("Error deleting review: " + e.getMessage());
            return StandardResponse.error("Failed to delete review", e.getMessage());
        }
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
