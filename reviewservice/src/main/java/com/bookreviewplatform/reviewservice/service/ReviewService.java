package com.bookreviewplatform.reviewservice.service;

import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.payloads.StandardResponse;

import java.util.UUID;

public interface ReviewService {
    StandardResponse getAllReviews();
    StandardResponse getReviewById(UUID id);
    StandardResponse getReviewsByBookId(UUID bookId);
    StandardResponse getReviewsByUserId(UUID userId);
    StandardResponse saveReview(ReviewRequestDTO reviewRequestDTO);
    StandardResponse deleteReview(UUID id);
}
