package com.bookreviewplatform.reviewservice.controller;

import com.bookreviewplatform.reviewservice.payloads.StandardResponse;
import com.bookreviewplatform.reviewservice.dto.ReviewDTO;
import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getAllReviews() {
        try {
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ReviewDTO>> getReviewById(@PathVariable UUID id) {
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            return ResponseEntity.ok(StandardResponse.success("Review retrieved successfully", review));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Review not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve review", e.getMessage()));
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getReviewsByBookId(@PathVariable UUID bookId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByBookId(bookId);
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getReviewsByUserId(@PathVariable UUID userId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<StandardResponse<ReviewDTO>> saveReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            ReviewDTO savedReview = reviewService.saveReview(reviewRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("Review created successfully", savedReview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create review", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<ReviewDTO>> updateReview(@PathVariable UUID id, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            ReviewDTO updatedReview = reviewService.updateReview(id, reviewRequestDTO);
            return ResponseEntity.ok(StandardResponse.success("Review updated successfully", updatedReview));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Review not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to update review", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteReview(@PathVariable UUID id) {
        try {
            Boolean deleted = reviewService.deleteReview(id);
            if (deleted) {
                return ResponseEntity.ok(StandardResponse.success("Review deleted successfully", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("Review not found", "Review with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete review", e.getMessage()));
        }
    }
}
