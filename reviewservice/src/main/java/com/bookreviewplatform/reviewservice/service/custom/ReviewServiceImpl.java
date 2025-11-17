package com.bookreviewplatform.reviewservice.service.custom;

import com.bookreviewplatform.reviewservice.dto.ReviewDTO;
import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.entity.Review;
import com.bookreviewplatform.reviewservice.repository.ReviewRepository;
import com.bookreviewplatform.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final WebClient bookWebClient;
    private final WebClient userWebClient;

    @Override
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        return convertToDTO(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByBookId(UUID bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO saveReview(ReviewRequestDTO reviewRequestDTO) {
        // Validate book exists
        validateBookExists(reviewRequestDTO.getBookId());
        
        // Validate user exists
        validateUserExists(reviewRequestDTO.getUserId());
        
        Review review = Review.builder()
                .rating(reviewRequestDTO.getRating())
                .bookId(reviewRequestDTO.getBookId())
                .userId(reviewRequestDTO.getUserId())
                .build();
        
        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Override
    public ReviewDTO updateReview(UUID id, ReviewRequestDTO reviewRequestDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        
        // Validate book exists if bookId is being changed
        if (!review.getBookId().equals(reviewRequestDTO.getBookId())) {
            validateBookExists(reviewRequestDTO.getBookId());
        }
        
        // Validate user exists if userId is being changed
        if (!review.getUserId().equals(reviewRequestDTO.getUserId())) {
            validateUserExists(reviewRequestDTO.getUserId());
        }
        
        review.setRating(reviewRequestDTO.getRating());
        review.setBookId(reviewRequestDTO.getBookId());
        review.setUserId(reviewRequestDTO.getUserId());
        
        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    @Override
    public Boolean deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            return false;
        }
        reviewRepository.deleteById(id);
        return true;
    }

    private void validateBookExists(UUID bookId) {
        try {
            bookWebClient.get()
                    .uri("/{id}", bookId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Book not found with id: " + bookId);
        }
    }

    private void validateUserExists(UUID userId) {
        try {
            userWebClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
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
