package com.bookreviewplatform.bookservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standardized, reusable API response wrapper used throughout the Book Service
 * — and ideally across the entire Book Review Platform microservices ecosystem.
 *
 * <p>Provides a predictable, consistent structure for all HTTP responses,
 * making client integration (frontend, mobile, third-party services) simple,
 * reliable, and less error-prone.</p>
 *
 * <h3>JSON Response Examples:</h3>
 * <pre>
 * Success:
 * {
 *   "success": true,
 *   "message": "Book created successfully",
 *   "data": { "id": "550e8400-...", "title": "Clean Code", "author": "Robert C. Martin" },
 *   "timestamp": "2025-12-01T10:30:45.123"
 * }
 *
 * Error:
 * {
 *   "success": false,
 *   "message": "Book not found",
 *   "error": "No book found with id: 550e8400-e29b-41d4-a716-446655440000",
 *   "timestamp": "2025-12-01T10:35:12.456"
 * }
 * </pre>
 *
 * @param <T> the type of the data payload (BookDTO, List<BookDTO>, String, etc.)
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse<T> {

    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;

    /**
     * Optional human-readable message (e.g., "Book updated", "Operation successful").
     * Commonly used in success responses.
     */
    private String message;

    /**
     * The actual business data returned by the API.
     * Null in error responses or when no data is relevant.
     */
    private T data;

    /**
     * Server timestamp when the response was generated.
     * Useful for logging, debugging, and client-side time reconciliation.
     */
    private LocalDateTime timestamp;

    /**
     * Detailed error description of the error (e.g., exception message, validation error).
     * Should be safe and not expose stack traces or internal details.
     */
    private String error;


    // ========================
    // Factory Methods
    // ========================

    /**
     * Creates a successful response with data and auto-generated timestamp.
     *
     * @param data the payload to return
     * @param <T>  type of data
     * @return StandardResponse with success = true
     */
    public static <T> StandardResponse<T> success(T data) {
        return StandardResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a successful response with custom message and data.
     *
     * @param message success message
     * @param data    payload
     * @param <T>     type of data
     * @return StandardResponse with message and data
     */
    public static <T> StandardResponse<T> success(String message, T data) {
        return StandardResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error response with a short error summary.
     *
     * @param error error description
     * @param <T>   type (usually irrelevant)
     * @return StandardResponse with success = false
     */
    public static <T> StandardResponse<T> error(String error) {
        return StandardResponse.<T>builder()
                .success(false)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error response with user-facing message and detailed error.
     *
     * @param message user-friendly message (e.g., "Unable to process request")
     * @param error   technical error detail
     * @param <T>     type
     * @return StandardResponse representing failure
     */
    public static <T> StandardResponse<T> error(String message, String error) {
        return StandardResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}