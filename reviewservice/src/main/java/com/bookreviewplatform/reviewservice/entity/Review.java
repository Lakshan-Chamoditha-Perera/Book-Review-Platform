package com.bookreviewplatform.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * JPA entity representing a book review in the Review Service.
 *
 * <p>Each instance corresponds to one review submitted by a user for a specific book.
 * This table acts as the source of truth for all ratings and review metadata
 * within the Book Review Platform.</p>
 *
 * <p><strong>Important:</strong> This entity stores only foreign keys ({@code bookId}, {@code userId})
 * to maintain loose coupling with the Book Service and User Service.
 * Actual book titles and usernames are fetched on-demand via inter-service calls
 * or can be denormalized in DTOs for performance.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {

    /**
     * Unique identifier for the review.
     * Automatically generated UUID to ensure global uniqueness across distributed systems.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Rating given by the user (typically 1–5 stars).
     * Stored as Integer for simplicity and to allow null during transient states if needed.
     *
     * <p>Validation (1 ≤ rating ≤ 5) should be enforced in the service layer or via Bean Validation.</p>
     */
    @Column(nullable = false)
    private Integer rating;

    /**
     * Foreign reference to the book being reviewed.
     * Matches the {@code id} field in the Book Service.
     *
     * <p>Not a JPA relationship (@ManyToOne) to avoid cross-service entity dependencies.</p>
     */
    @Column(nullable = false, updatable = false)
    private UUID bookId;

    /**
     * Foreign reference to the user who submitted the review.
     * Matches the {@code id} from the User Service.
     *
     * <p>Not a JPA relationship — keeps Review Service independent and resilient.</p>
     */
    @Column(nullable = false, updatable = false)
    private UUID userId;
}