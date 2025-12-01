package com.bookreviewplatform.reviewservice.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object representing minimal book information
 * received from the <strong>Book Service</strong> via inter-service communication.
 *
 * <p>This DTO is used within the Review Service to validate book existence,
 * display book context in reviews, and avoid tight coupling with the full
 * Book entity from the Book Service.</p>
 *
 * <p>Only essential fields are included to keep payloads lightweight
 * and maintain clear service boundaries.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {

    /**
     * Unique identifier of the book in the Book Service.
     * Must match the book's UUID in the central book catalog.
     */
    private UUID id;

    /**
     * Title of the book.
     * Used for display purposes in review listings and validation messages.
     */
    private String title;

    /**
     * Author(s) of the book.
     */
    private String author;
}