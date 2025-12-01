package com.bookreviewplatform.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object representing a book review in the Review Service.
 *
 * <p>This DTO is used to transfer review data between the Review Service and clients
 * (e.g., frontend or other microservices) or within the service itself (e.g., between
 * controller and service layers). It includes essential review details while keeping
 * payloads lightweight and focused.</p>
 *
 * <p>Typically used for responses (e.g., listing reviews) or internal mappings
 * after fetching data from the {@code ReviewEntity} or external services.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    /**
     * Unique identifier of the review.
     * Generated automatically when the review is created.
     */
    private UUID id;

    /**
     * Rating given by the user for the book (e.g., 1 to 5 stars).
     * Should be validated to ensure it falls within an acceptable range.
     */
    private Integer rating;

    /**
     * Unique identifier of the book being reviewed.
     * References a book in the Book Service and is used to validate book existence.
     */
    private UUID bookId;

    /**
     * Unique identifier of the user who wrote the review.
     * References a user in the User Service and is used to validate user existence.
     */
    private UUID userId;
}