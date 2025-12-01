package com.bookreviewplatform.bookservice.dto;

import lombok.*;

/**
 * Data Transfer Object used for creating or updating a book via REST API.
 *
 * <p>This DTO is bound to the request body in {@code POST /api/v1/books}
 * and {@code PUT /api/v1/books/{id}} endpoints. It contains only the fields
 * that clients are allowed to set when managing books.</p>
 *
 * <p>Unlike {@link BookDTO}, this class does <strong>not</strong> include the
 * auto-generated {@code id} field, enforcing correct separation between
 * input (request) and output (response) contracts.</p>
 *
 * <p>Used exclusively in {@link com.bookreviewplatform.bookservice.controller.BookController}
 * for create and update operations.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookRequestDTO {

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Name of the author(s).
     * <p>Example values: "J.K. Rowling", "George R.R. Martin", "Agatha Christie"</p>
     */
    private String author;
}