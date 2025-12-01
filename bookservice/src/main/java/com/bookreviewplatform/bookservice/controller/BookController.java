package com.bookreviewplatform.bookservice.controller;

import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import com.bookreviewplatform.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * REST controller responsible for managing book-related HTTP operations
 * in the Book Review Platform.
 *
 * <p>Provides full CRUD endpoints for books and serves as the central catalog
 * for all book data used across the platform (e.g., by Review Service).</p>
 *
 * <p>All endpoints are prefixed with {@code /api/v1/books} and return responses
 * wrapped in {@link StandardResponse} for consistent, predictable API behavior.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    private final BookService bookService;

    /**
     * Retrieves all books in the catalog.
     *
     * @return ResponseEntity with list of books and HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<StandardResponse> getAllBooks() {
        logger.info("Received request to get all books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Retrieves a single book by its unique identifier.
     *
     * @param id the UUID of the book
     * @return ResponseEntity with book details or error if not found (404 via exception handler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getBookById(@PathVariable UUID id) {
        logger.info("Received request to get book by id: " + id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * Creates a new book in the catalog.
     *
     * @param bookRequestDTO contains title, author, isbn, publicationYear, etc.
     * @return ResponseEntity with created book and success message
     * <p><strong>Recommendation:</strong> Use HTTP 201 Created for resource creation</p>
     */
    @PostMapping
    public ResponseEntity<StandardResponse> saveBook(@RequestBody BookRequestDTO bookRequestDTO) {
        logger.info("Received request to create book with title: " + bookRequestDTO.getTitle());
        StandardResponse response = bookService.saveBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        // More RESTful than returning 200 OK for creation
    }

    /**
     * Updates an existing book identified by ID.
     *
     * <p>Performs full or partial update based on provided fields.
     * Throws BookNotFoundException if book does not exist.</p>
     *
     * @param id             the UUID of the book to update
     * @param bookRequestDTO updated book data
     * @return ResponseEntity with updated book details
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateBook(
            @PathVariable UUID id,
            @RequestBody BookRequestDTO bookRequestDTO) {

        logger.info("Received request to update book with id: " + id);
        return ResponseEntity.ok(bookService.updateBook(id, bookRequestDTO));
    }

    /**
     * Deletes a book permanently by its unique identifier.
     *
     * <p>In production, consider soft delete or restrict to admin users only.</p>
     *
     * @param id the UUID of the book to delete
     * @return ResponseEntity confirming deletion or error if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteBook(@PathVariable UUID id) {
        logger.info("Received request to delete book with id: " + id);
        return ResponseEntity.ok(bookService.deleteBook(id));
    }
}