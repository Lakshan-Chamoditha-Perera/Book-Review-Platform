package com.bookreviewplatform.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object used to receive review creation requests from clients.
 *
 * <p>This DTO is bound to the request body of POST {@code /api/v1/reviews}
 * and contains the minimal required information to create a new book review.</p>
 *
 * <p>The Review Service uses this object to:</p>
 * <ul>
 *   <li>Validate that the rating is within allowed range (typically 1–5)</li>
 *   <li>Verify that the {@code bookId} exists in the Book Service</li>
 *   <li>Verify that the {@code userId} exists in the User Service</li>
 *   <li>Enforce business rules (e.g., one review per user per book)</li>
 * </ul>
 *
 * <p>Note: The review ID is not included — it is auto-generated on creation.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    /**
     * The rating given by the user to the book.
     * Expected values: 1 to 5 (inclusive).
     * Should be validated using {@code @Min(1) @Max(5)} or in service logic.
     */
    private Integer rating;

    /**
     * Unique identifier of the book being reviewed.
     * Must correspond to an existing book in the Book Service.
     */
    private UUID bookId;

    /**
     * Unique identifier of the user submitting the review.
     * Must correspond to an authenticated and existing user in the User Service.
     */
    private UUID userId;
}