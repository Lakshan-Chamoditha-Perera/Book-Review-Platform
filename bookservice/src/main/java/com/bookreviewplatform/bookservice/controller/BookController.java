package com.bookreviewplatform.bookservice.controller;

import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import com.bookreviewplatform.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final Logger logger = Logger.getLogger(BookController.class.getName());
    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<StandardResponse> getAllBooks() {
        logger.info("Received request to get all books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getBookById(@PathVariable UUID id) {
        logger.info("Received request to get book by id: " + id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping()
    public ResponseEntity<StandardResponse> saveBook(@RequestBody BookRequestDTO bookRequestDTO) {
        logger.info("Received request to create book with title: " + bookRequestDTO.getTitle());
        return ResponseEntity.ok(bookService.saveBook(bookRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateBook(@PathVariable UUID id, @RequestBody BookRequestDTO bookRequestDTO) {
        logger.info("Received request to update book with id: " + id);
        return ResponseEntity.ok(bookService.updateBook(id, bookRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteBook(@PathVariable UUID id) {
        logger.info("Received request to delete book with id: " + id);
        return ResponseEntity.ok(bookService.deleteBook(id));
    }
}
