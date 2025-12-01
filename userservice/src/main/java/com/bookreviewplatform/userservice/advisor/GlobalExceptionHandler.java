package com.bookreviewplatform.userservice.advisor;

import com.bookreviewplatform.userservice.exception.UserNotFoundException;
import com.bookreviewplatform.userservice.payloads.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the User Service module.
 *
 * <p>This class uses Spring's {@link ControllerAdvice} to centralize exception handling
 * across all {@code @RestController} classes in the {@code com.bookreviewplatform.userservice} package
 * (and sub-packages). It converts exceptions into consistent {@link StandardResponse} payloads
 * with appropriate HTTP status codes.</p>
 *
 * <p>Benefits:</p>
 * <ul>
 *   <li>Consistent error response format across the entire API</li>
 *   <li>Clean controllers (no try-catch blocks needed)</li>
 *   <li>Easy to extend with new exception types</li>
 * </ul>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link UserNotFoundException} thrown when a user cannot be found by ID, email, or other criteria.
     *
     * @param ex      the thrown {@link UserNotFoundException}
     * @param request the current web request (useful for logging or extracting headers/context)
     * @return a {@link ResponseEntity} containing a {@link StandardResponse} with error details
     * and HTTP status {@code 404 Not Found}
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardResponse<Object>> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {

        StandardResponse<Object> response = StandardResponse.error(
                "User not found",
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Fallback handler for any uncaught exceptions (acts as a safety net).
     *
     * <p>This prevents stack traces from being exposed in production and ensures
     * the client always receives a structured {@link StandardResponse}.</p>
     *
     * <p><strong>Note:</strong> In production, you may want to log the full stack trace
     * (using a proper logger) while returning a generic message to the client.</p>
     *
     * @param ex      the unexpected exception
     * @param request the current web request
     * @return a {@link ResponseEntity} with HTTP status {@code 500 Internal Server Error}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        // TODO: Log the full exception with a proper logger (e.g., SLF4J + Logback)
        // log.error("Unexpected error occurred", ex);

        StandardResponse<Object> response = StandardResponse.error(
                "Internal server error",
                "An unexpected error occurred. Please try again later."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}