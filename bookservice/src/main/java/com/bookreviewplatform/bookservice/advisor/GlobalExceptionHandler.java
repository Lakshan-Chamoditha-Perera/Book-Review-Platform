package com.bookreviewplatform.bookservice.advisor;

import com.bookreviewplatform.bookservice.exception.BookNotFoundException;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the Book Service module.
 *
 * <p>Centralizes exception handling using {@link ControllerAdvice} to ensure
 * consistent, structured error responses across all controllers in the
 * {@code com.bookreviewplatform.bookservice} package.</p>
 *
 * <p>All error responses are wrapped in {@link StandardResponse} with appropriate
 * HTTP status codes, maintaining API consistency and preventing raw stack traces
 * from leaking to clients.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link BookNotFoundException} thrown when a book cannot be found
     * by ID, ISBN, or other criteria.
     *
     * @param ex      the thrown {@link BookNotFoundException}
     * @param request the current web request (for context/logging if needed)
     * @return {@link ResponseEntity} with structured error and HTTP 404 Not Found
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<StandardResponse<Object>> handleBookNotFoundException(
            BookNotFoundException ex, WebRequest request) {

        StandardResponse<Object> response = StandardResponse.error(
                "Book not found",
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Fallback handler for any unexpected exceptions (safety net).
     *
     * <p>Prevents exposure of internal details in production while ensuring
     * clients always receive a predictable, structured response.</p>
     *
     * <p><strong>Production Tip:</strong> Log the full exception server-side
     * using SLF4J/Logback but return only generic messages to clients.</p>
     *
     * @param ex      the unexpected exception
     * @param request the current web request
     * @return {@link ResponseEntity} with HTTP 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        // TODO: Replace with proper logging in production
        // log.error("Unexpected error in Book Service", ex);

        StandardResponse<Object> response = StandardResponse.error(
                "Internal server error",
                "An unexpected error occurred. Please try again later or contact support."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}