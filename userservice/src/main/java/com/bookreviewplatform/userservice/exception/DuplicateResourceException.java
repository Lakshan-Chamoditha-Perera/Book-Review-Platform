package com.bookreviewplatform.userservice.exception;

/**
 * Thrown when attempting to create or update a resource that would violate uniqueness constraints
 * (for example: email already in use or id already exists).
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}