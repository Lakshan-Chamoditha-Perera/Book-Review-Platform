package com.bookreviewplatform.reviewservice.service;

import com.bookreviewplatform.reviewservice.dto.ReviewRequestDTO;
import com.bookreviewplatform.reviewservice.payloads.StandardResponse;

import java.util.UUID;

/**
 * Service layer interface defining all business operations related to book reviews
 * in the Book Review Platform.
 *
 * <p>Acts as the central contract between the {@code ReviewController} and the data/repository layer,
 * encapsulating business rules, validation, inter-service communication, and response formatting.</p>
 *
 * <p>Key responsibilities include:</p>
 * <ul>
 *   <li>Validating that books and users exist (via Book/User Service)</li>
 *   <li>Enforcing "one review per user per book" policy</li>
 *   <li>Mapping between entities and DTOs</li>
 *   <li>Returning consistent {@link StandardResponse} objects</li>
 * </ul>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public interface ReviewService {

    /**
     * Retrieves all reviews in the system.
     *
     * @return {@link StandardResponse} containing a list of reviews (typically as {@code ReviewDTO})
     */
    StandardResponse getAllReviews();

    /**
     * Retrieves a single review by its unique identifier.
     *
     * @param id the UUID of the review
     * @return {@link StandardResponse} with the review details or error if not found
     */
    StandardResponse getReviewById(UUID id);

    /**
     * Retrieves all reviews for a specific book.
     * Commonly used to display reviews on a book's detail page.
     *
     * @param bookId the UUID of the book
     * @return {@link StandardResponse} containing list of reviews for the book
     */
    StandardResponse getReviewsByBookId(UUID bookId);

    /**
     * Retrieves all reviews written by a specific user.
     * Used in user profile or activity feed.
     *
     * @param userId the UUID of the user
     * @return {@link StandardResponse} containing the user's review history
     */
    StandardResponse getReviewsByUserId(UUID userId);

    /**
     * Creates a new review based on the provided request data.
     *
     * <p>Business rules applied:</p>
     * <ul>
     *   <li>Rating must be between 1 and 5</li>
     *   <li>Book with {@code bookId} must exist in Book Service</li>
     *   <li>User with {@code userId} must exist in User Service</li>
     *   <li>User must not have already reviewed this book</li>
     * </ul>
     *
     * @param reviewRequestDTO contains rating, bookId, and userId
     * @return {@link StandardResponse} with the created review and success message
     * (typically triggers HTTP 201 Created in controller)
     */
    StandardResponse saveReview(ReviewRequestDTO reviewRequestDTO);

    /**
     * Deletes a review by its unique identifier.
     *
     * <p>In a production system, this should be restricted to:</p>
     * <ul>
     *   <li>The review author</li>
     *   <li>System administrators</li>
     * </ul>
     * <p>Consider soft delete in the future.</p>
     *
     * @param id the UUID of the review to delete
     * @return {@link StandardResponse} confirming deletion or reporting error (e.g., not found)
     */
    StandardResponse deleteReview(UUID id);
}