package com.bookreviewplatform.userservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standardized API response wrapper used across the User Service (and ideally the entire Book Review Platform).
 *
 * <p>Provides a consistent, predictable response structure for all REST endpoints,
 * making it easier for frontend clients and third-party integrations to handle success
 * and error cases uniformly.</p>
 *
 * <h3>Response Structure Examples:</h3>
 * <pre>
 * Success:
 * {
 *   "success": true,
 *   "message": "User retrieved successfully",
 *   "data": { ...user object... },
 *   "timestamp": "2025-12-01T10:30:45.123"
 * }
 *
 * Error:
 * {
 *   "success": false,
 *   "message": "User not found",
 *   "error": "No user found with email: john@example.com",
 *   "timestamp": "2025-12-01T10:30:45.123"
 * }
 * </pre>
 *
 * @param <T> the type of the data payload (can be any object, list, or null)
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse<T> {

    /**
     * Indicates whether the request was successful.
     */
    private boolean success;

    /**
     * Optional human-readable message describing the outcome.
     * Commonly used for success messages or high-level error context.
     */
    private String message;

    /**
     * The actual data returned by the API (e.g., User DTO, list of users, etc.).
     * May be {@code null} in error responses or when no data is relevant.
     */
    private T data;

    /**
     * Timestamp of when the response was generated (server time).
     * Useful for logging, debugging, and client-side caching strategies.
     */
    private LocalDateTime timestamp;

    /**
     * Detailed error message or code in case of failure.
     * Should be technical enough for developers but safe (no stack traces).
     */
    private String error;

    // ========================
    // Factory Methods
    // ========================

    /**
     * Creates a successful response with data and auto-generated timestamp.
     *
     * @param data the payload to return
     * @param <T>  type of the data
     * @return StandardResponse with success = true and current timestamp
     */
    public static <T> StandardResponse<T> success(T data) {
        return StandardResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a successful response with a custom message and data.
     *
     * @param message descriptive success message
     * @param data    the payload to return
     * @param <T>     type of the data
     * @return StandardResponse with custom message
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
     * Creates an error response with a short error identifier.
     *
     * @param error short error summary (e.g., "User not found")
     * @param <T>   type of data (usually irrelevant in errors)
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
     * Creates an error response with both a user-friendly message and detailed error.
     *
     * @param message user-facing message (e.g., "Unable to process request")
     * @param error   detailed/technical error (e.g., exception message or code)
     * @param <T>     type of data
     * @return StandardResponse representing a failed operation
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