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
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final Logger logger = Logger.getLogger(BookController.class.getName());
    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<BookDTO>>> getAllBooks() {
        logger.info("Received request to get all books");
        try {
            List<BookDTO> books = bookService.getAllBooks();
            logger.info("Successfully retrieved " + books.size() + " books");
            return ResponseEntity.ok(StandardResponse.success("Books retrieved successfully", books));
        } catch (Exception e) {
            logger.severe("Failed to retrieve books: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve books", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<BookDTO>> getBookById(@PathVariable UUID id) {
        logger.info("Received request to get book by id: " + id);
        try {
            BookDTO book = bookService.getBookById(id);
            logger.info("Successfully retrieved book with id: " + id);
            return ResponseEntity.ok(StandardResponse.success("Book retrieved successfully", book));
        } catch (RuntimeException e) {
            logger.warning("Book not found with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Book not found", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Failed to retrieve book with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve book", e.getMessage()));
        }
    }



    @PostMapping()
    public ResponseEntity<StandardResponse<BookDTO>> saveBook(@RequestBody BookRequestDTO bookRequestDTO) {
        logger.info("Received request to create book with title: " + bookRequestDTO.getTitle());
        try {
            BookDTO savedBook = bookService.saveBook(bookRequestDTO);
            logger.info("Successfully created book with id: " + savedBook.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("Book created successfully", savedBook));
        } catch (Exception e) {
            logger.severe("Failed to create book with title " + bookRequestDTO.getTitle() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create book", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<BookDTO>> updateBook(@PathVariable UUID id, @RequestBody BookRequestDTO bookRequestDTO) {
        logger.info("Received request to update book with id: " + id);
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
            logger.info("Successfully updated book with id: " + id);
            return ResponseEntity.ok(StandardResponse.success("Book updated successfully", updatedBook));
        } catch (RuntimeException e) {
            logger.warning("Book not found with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("Book not found", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Failed to update book with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to update book", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteBook(@PathVariable UUID id) {
        logger.info("Received request to delete book with id: " + id);
        try {
            Boolean deleted = bookService.deleteBook(id);
            if (deleted) {
                logger.info("Successfully deleted book with id: " + id);
                return ResponseEntity.ok(StandardResponse.success("Book deleted successfully", true));
            } else {
                logger.warning("Book not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("Book not found", "Book with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            logger.severe("Failed to delete book with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete book", e.getMessage()));
        }
    }
}
