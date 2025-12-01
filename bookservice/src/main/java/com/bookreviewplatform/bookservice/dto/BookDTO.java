package com.bookreviewplatform.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object representing a book in the Book Service.
 *
 * <p>This DTO is used for both request and response payloads in REST operations
 * and serves as the primary contract for book data exchanged with clients
 * (frontend, mobile apps, Review Service, etc.).</p>
 *
 * <p>It includes only the essential, stable fields required across the platform,
 * keeping payloads lightweight and focused while allowing future extension.</p>
 *
 * <p>Used in:</p>
 * <ul>
 *   <li>Creating/updating books via {@code POST /api/v1/books} and {@code PUT /api/v1/books/{id}}</li>
 *   <li>Returning book details in list and single-resource responses</li>
 *   <li>Inter-service communication (e.g., sent to Review Service for validation/display)</li>
 * </ul>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    /**
     * Unique identifier of the book.
     * Auto-generated UUID when a book is created. Never null in responses.
     */
    private UUID id;

    /**
     * Title of the book.
     * Required field. Should be unique or validated with ISBN in production.
     */
    private String title;

    /**
     * Name of the author(s).
     * Can be a single name or comma-separated list (e.g., "J.R.R. Tolkien").
     * Consider normalizing to a separate Author entity in future versions.
     */
    private String author;
}