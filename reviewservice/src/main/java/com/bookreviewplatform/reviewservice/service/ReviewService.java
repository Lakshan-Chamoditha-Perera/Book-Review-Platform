package com.bookreviewplatform.reviewservice.service;

import com.bookreviewplatform.reviewservice.dto.ReviewDTO;
import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    ReviewDTO getReviewById(UUID id);
    List<ReviewDTO> getReviewsByBookId(UUID bookId);
    List<ReviewDTO> getReviewsByUserId(UUID userId);
    ReviewDTO saveReview(ReviewRequestDTO reviewRequestDTO);
    ReviewDTO updateReview(UUID id, ReviewRequestDTO reviewRequestDTO);
    Boolean deleteReview(UUID id);
}
