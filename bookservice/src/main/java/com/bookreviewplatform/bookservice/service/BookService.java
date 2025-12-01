package com.bookreviewplatform.bookservice.service;

import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;

import java.util.UUID;

/**
 * Service layer interface defining all business operations for managing books
 * in the Book Review Platform.
 *
 * <p>Acts as the central contract between {@code BookController} and the repository layer,
 * encapsulating business logic, validation, validation, duplicate prevention, and consistent response formatting.</p>
 *
 * <p>Key responsibilities include:</p>
 * <ul>
 *   <li>Enforcing logical uniqueness (same title + author = duplicate book)</li>
 *   <li>Validating input data before persistence</li>
 *   <li>Throwing {@link com.bookreviewplatform.bookservice.exception.BookNotFoundException} when appropriate</li>
 *   <li>Returning structured {@link StandardResponse} objects for API consistency</li>
 *   <li>Mapping between {@link com.bookreviewplatform.bookservice.entity.Book} and DTOs</li>
 * </ul>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public interface BookService {

    /**
     * Retrieves all books currently registered in the catalog.
     *
     * @return {@link StandardResponse} containing a list of books (as {@code BookDTO})
     */
    StandardResponse getAllBooks();

    /**
     * Retrieves a single book by its unique identifier.
     *
     * @param id the {@link UUID} of the book
     * @return {@link StandardResponse} with book details
     * @throws com.bookreviewplatform.bookservice.exception.BookNotFoundException if no book exists with the given ID
     */
    StandardResponse getBookById(UUID id);

    /**
     * Creates a new book in the catalog.
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *   <li>Title and author must not be blank</li>
     *   <li>No existing book with the same title and author (case-sensitive)</li>
     * </ul>
     *
     * @param bookRequestDTO contains title and author
     * @return {@link StandardResponse} with the created book and success message
     * (used with HTTP 201 Created in controller)
     */
    StandardResponse saveBook(BookRequestDTO bookRequestDTO);

    /**
     * Updates an existing book identified by ID.
     *
     * <p>Supports full replacement of title and author.
     * Enforces same uniqueness rule as creation.</p>
     *
     * @param id             the {@link UUID} of the book to update
     * @param bookRequestDTO new title and author values
     * @return {@link StandardResponse} with updated book data
     * @throws com.bookreviewplatform.bookservice.exception.BookNotFoundException if book not found
     */
    StandardResponse updateBook(UUID id, BookRequestDTO bookRequestDTO);

    /**
     * Permanently deletes a book by its unique identifier.
     *
     * <p>In production environments, this operation should be restricted to administrators
     * and may trigger cascading actions (e.g., soft delete, archive reviews, etc.).</p>
     *
     * @param id the {@link UUID} of the book to delete
     * @return {@link StandardResponse} confirming successful deletion
     * @throws com.bookreviewplatform.bookservice.exception.BookNotFoundException if book not found
     */
    StandardResponse deleteBook(UUID id);
}