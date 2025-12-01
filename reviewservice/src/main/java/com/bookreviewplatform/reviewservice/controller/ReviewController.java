package com.bookreviewplatform.reviewservice.controller;

import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.payloads.StandardResponse;
import com.bookreviewplatform.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * REST controller responsible for handling book review-related HTTP requests
 * in the Book Review Platform.
 *
 * <p>All endpoints are prefixed with {@code /api/v1/reviews} and return responses
 * wrapped in {@link StandardResponse} for consistent API structure.</p>
 *
 * <p>Supports retrieving reviews by various criteria, creating new reviews,
 * and deleting existing ones.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private static final Logger logger = Logger.getLogger(ReviewController.class.getName());

    private final ReviewService reviewService;

    /**
     * Retrieves all reviews in the system.
     *
     * @return ResponseEntity containing a list of all reviews wrapped in StandardResponse
     * with HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<StandardResponse> getAllReviews() {
        logger.info("Received request to get all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /**
     * Retrieves a specific review by its unique identifier.
     *
     * @param id the UUID of the review to retrieve
     * @return ResponseEntity with the review details or error if not found (HTTP 200 or 404 via service)
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getReviewById(@PathVariable UUID id) {
        logger.info("Received request to get review by id: " + id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId the UUID of the book
     * @return ResponseEntity containing a list of reviews for the given book
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<StandardResponse> getReviewsByBookId(@PathVariable UUID bookId) {
        logger.info("Received request to get reviews for book id: " + bookId);
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }

    /**
     * Retrieves all reviews written by a specific user.
     *
     * @param userId the UUID of the user
     * @return ResponseEntity containing a list of reviews authored by the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse> getReviewsByUserId(@PathVariable UUID userId) {
        logger.info("Received request to get reviews for user id: " + userId);
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    /**
     * Creates a new book review.
     *
     * <p>Expects a valid {@link ReviewRequestDTO} containing rating, comment,
     * bookId, and userId. The service layer should validate book/user existence
     * and enforce business rules (e.g., one review per user per book).</p>
     *
     * @param reviewRequestDTO the review data to save
     * @return ResponseEntity with the created review and HTTP 200 OK
     * (Consider changing to 201 Created if preferred)
     */
    @PostMapping
    public ResponseEntity<StandardResponse> saveReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        logger.info("Received request to create review for book id: " + reviewRequestDTO.getBookId() +
                " by user id: " + reviewRequestDTO.getUserId());
        return ResponseEntity.ok(reviewService.saveReview(reviewRequestDTO));
        // Alternative (more RESTful):
        // return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.saveReview(reviewRequestDTO));
    }

    /**
     * Deletes a review by its unique identifier.
     *
     * <p>Typically restricted to admins or the review's author in production.</p>
     *
     * @param id the UUID of the review to delete
     * @return ResponseEntity confirming deletion or reporting error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteReview(@PathVariable UUID id) {
        logger.info("Received request to delete review with id: " + id);
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}