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
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getAllReviews() {
        logger.info("Received request to get all reviews");
        try {
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            logger.info("Successfully retrieved " + reviews.size() + " reviews");
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            logger.severe("Failed to retrieve reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ReviewDTO>> getReviewById(@PathVariable UUID id) {
        logger.info("Received request to get review by id: " + id);
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            logger.info("Successfully retrieved review with id: " + id);
            return ResponseEntity.ok(StandardResponse.success("Review retrieved successfully", review));
        } catch (RuntimeException e) {
            logger.warning("Review not found with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Review not found", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Failed to retrieve review with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve review", e.getMessage()));
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getReviewsByBookId(@PathVariable UUID bookId) {
        logger.info("Received request to get reviews for book id: " + bookId);
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByBookId(bookId);
            logger.info("Successfully retrieved " + reviews.size() + " reviews for book id: " + bookId);
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            logger.severe("Failed to retrieve reviews for book id " + bookId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getReviewsByUserId(@PathVariable UUID userId) {
        logger.info("Received request to get reviews for user id: " + userId);
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            logger.info("Successfully retrieved " + reviews.size() + " reviews for user id: " + userId);
            return ResponseEntity.ok(StandardResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            logger.severe("Failed to retrieve reviews for user id " + userId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve reviews", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<StandardResponse<ReviewDTO>> saveReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        logger.info("Received request to create review for book id: " + reviewRequestDTO.getBookId() + 
                    " by user id: " + reviewRequestDTO.getUserId());
        try {
            ReviewDTO savedReview = reviewService.saveReview(reviewRequestDTO);
            logger.info("Successfully created review with id: " + savedReview.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("Review created successfully", savedReview));
        } catch (Exception e) {
            logger.severe("Failed to create review for book id " + reviewRequestDTO.getBookId() + 
                         " by user id " + reviewRequestDTO.getUserId() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create review", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteReview(@PathVariable UUID id) {
        logger.info("Received request to delete review with id: " + id);
        try {
            Boolean deleted = reviewService.deleteReview(id);
            if (deleted) {
                logger.info("Successfully deleted review with id: " + id);
                return ResponseEntity.ok(StandardResponse.success("Review deleted successfully", true));
            } else {
                logger.warning("Review not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("Review not found", "Review with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            logger.severe("Failed to delete review with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete review", e.getMessage()));
        }
    }
}
