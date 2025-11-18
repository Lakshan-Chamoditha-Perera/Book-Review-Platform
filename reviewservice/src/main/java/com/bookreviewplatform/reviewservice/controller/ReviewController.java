package com.bookreviewplatform.reviewservice.controller;

import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.payloads.StandardResponse;
import com.bookreviewplatform.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<StandardResponse> getAllReviews() {
        logger.info("Received request to get all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());

    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getReviewById(@PathVariable UUID id) {
        logger.info("Received request to get review by id: " + id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<StandardResponse> getReviewsByBookId(@PathVariable UUID bookId) {
        logger.info("Received request to get reviews for book id: " + bookId);
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse> getReviewsByUserId(@PathVariable UUID userId) {
        logger.info("Received request to get reviews for user id: " + userId);
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    @PostMapping()
    public ResponseEntity<StandardResponse> saveReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        logger.info("Received request to create review for book id: " + reviewRequestDTO.getBookId() +
                " by user id: " + reviewRequestDTO.getUserId());
        return ResponseEntity.ok(reviewService.saveReview(reviewRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteReview(@PathVariable UUID id) {
        logger.info("Received request to delete review with id: " + id);
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
