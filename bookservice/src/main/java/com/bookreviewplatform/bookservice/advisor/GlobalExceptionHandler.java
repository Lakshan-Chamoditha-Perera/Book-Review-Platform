package com.bookreviewplatform.bookservice.advisor;

import com.bookreviewplatform.bookservice.exception.BookNotFoundException;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle specific exception :- BookNotFoundException
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<StandardResponse<Object>> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        StandardResponse<Object> response = StandardResponse.error("Book not found", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
        StandardResponse<Object> response = StandardResponse.error("Internal server error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
