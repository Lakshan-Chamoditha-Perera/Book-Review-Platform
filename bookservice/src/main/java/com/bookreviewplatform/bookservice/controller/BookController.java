package com.bookreviewplatform.bookservice.controller;

import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import com.bookreviewplatform.bookservice.dto.BookDTO;
import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<BookDTO>>> getAllBooks() {
        try {
            List<BookDTO> books = bookService.getAllBooks();
            return ResponseEntity.ok(StandardResponse.success("Books retrieved successfully", books));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve books", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<BookDTO>> getBookById(@PathVariable UUID id) {
        try {
            BookDTO book = bookService.getBookById(id);
            return ResponseEntity.ok(StandardResponse.success("Book retrieved successfully", book));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Book not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve book", e.getMessage()));
        }
    }



    @PostMapping()
    public ResponseEntity<StandardResponse<BookDTO>> saveBook(@RequestBody BookRequestDTO bookRequestDTO) {
        try {
            BookDTO savedBook = bookService.saveBook(bookRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("Book created successfully", savedBook));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create book", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<BookDTO>> updateBook(@PathVariable UUID id, @RequestBody BookRequestDTO bookRequestDTO) {
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
            return ResponseEntity.ok(StandardResponse.success("Book updated successfully", updatedBook));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Book not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to update book", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteBook(@PathVariable UUID id) {
        try {
            Boolean deleted = bookService.deleteBook(id);
            if (deleted) {
                return ResponseEntity.ok(StandardResponse.success("Book deleted successfully", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("Book not found", "Book with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete book", e.getMessage()));
        }
    }
}
