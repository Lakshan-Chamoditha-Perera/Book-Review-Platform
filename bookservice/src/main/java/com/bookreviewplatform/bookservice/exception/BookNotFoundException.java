package com.bookreviewplatform.bookservice.exception;

/**
 * Custom unchecked exception thrown when a requested book cannot be found
 * in the Book Service database.
 *
 * <p>This exception is typically triggered during operations such as:</p>
 * <ul>
 *   <li>Retrieving a book by ID ({@code GET /api/v1/books/{id}})</li>
 *   <li>Updating a book ({@code PUT /api/v1/books/{id}})</li>
 *   <li>Deleting a book ({@code DELETE /api/v1/books/{id}})</li>
 *   <li>Validating book existence from other services (e.g., Review Service)</li>
 * </ul>
 *
 * <p>It is handled by {@link com.bookreviewplatform.bookservice.advisor.GlobalExceptionHandler}
 * and converted into a structured {@code StandardResponse} with HTTP 404 Not Found.</p>
 *
 * <p>Being a {@link RuntimeException}, it does not need to be declared in method signatures,
 * keeping service and controller code clean and focused on happy paths.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message (e.g., "Book not found with id: 550e8400-e29b-41d4-a716-446655440000")
     *                The message is saved for later retrieval by {@link #getMessage()}
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Convenience constructor for common use case: book not found by UUID.
     *
     * @param id the UUID that was not found
     */
    public BookNotFoundException(java.util.UUID id) {
        super("Book not found with id: " + id);
    }
}