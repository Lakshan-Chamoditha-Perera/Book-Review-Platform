package com.bookreviewplatform.bookservice.service.custom;

import com.bookreviewplatform.bookservice.dto.BookDTO;
import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.entity.Book;
import com.bookreviewplatform.bookservice.exception.BookNotFoundException;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;
import com.bookreviewplatform.bookservice.repository.BookRepository;
import com.bookreviewplatform.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final Logger logger = Logger.getLogger(BookServiceImpl.class.getName());
    private final BookRepository bookRepository;

    @Override
    public StandardResponse getAllBooks() {
        try {
            logger.fine("Fetching all books from database");
            List<BookDTO> books = bookRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logger.fine("Found " + books.size() + " books in database");
            return StandardResponse.success("Books retrieved successfully", books);
        } catch (Exception e) {
            logger.severe("Error fetching books: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve books", e.getMessage());
        }
    }

    @Override
    public StandardResponse getBookById(UUID id) {
        try {
            logger.fine("Searching for book with id: " + id);
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.severe("Book not found with id: " + id);
                        return new BookNotFoundException("Book not found with id: " + id);
                    });
            logger.fine("Book found with id: " + id);
            return StandardResponse.success("Book retrieved successfully", convertToDTO(book));
        } catch (RuntimeException e) {
            logger.warning("Book not found with id: " + id);
            return StandardResponse.error("Book not found", e.getMessage());
        } catch (Exception e) {
            logger.severe("Error retrieving book: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve book", e.getMessage());
        }
    }


    @Override
    public StandardResponse saveBook(BookRequestDTO bookRequestDTO) {
        try {
            logger.fine("Creating new book with title: " + bookRequestDTO.getTitle() +
                    " by author: " + bookRequestDTO.getAuthor());

            Book book = Book.builder()
                    .title(bookRequestDTO.getTitle())
                    .author(bookRequestDTO.getAuthor())
                    .build();

            Book savedBook = bookRepository.save(book);
            logger.info("Book created successfully with id: " + savedBook.getId());
            return StandardResponse.success("Book created successfully", convertToDTO(savedBook));
        } catch (Exception e) {
            logger.severe("Error creating book: " + e.getMessage());
            return StandardResponse.error("Failed to create book", e.getMessage());
        }
    }

    @Override
    public StandardResponse updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        try {
            logger.fine("Updating book with id: " + id);
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.severe("Book not found with id: " + id);
                        return new BookNotFoundException("Book not found with id: " + id);
                    });

            logger.fine("Updating book details - Title: " + bookRequestDTO.getTitle() +
                    ", Author: " + bookRequestDTO.getAuthor());
            book.setTitle(bookRequestDTO.getTitle());
            book.setAuthor(bookRequestDTO.getAuthor());

            Book updatedBook = bookRepository.save(book);
            logger.info("Book updated successfully with id: " + id);
            return StandardResponse.success("Book updated successfully", convertToDTO(updatedBook));
        } catch (RuntimeException e) {
            logger.warning("Book not found with id: " + id);
            return StandardResponse.error("Book not found", e.getMessage());
        } catch (Exception e) {
            logger.severe("Error updating book: " + e.getMessage());
            return StandardResponse.error("Failed to update book", e.getMessage());
        }
    }

    @Override
    public StandardResponse deleteBook(UUID id) {
        try {
            logger.fine("Checking if book exists with id: " + id);
            if (!bookRepository.existsById(id)) {
                logger.warning("Book not found with id: " + id);
                return StandardResponse.error("Book not found", "Book with id " + id + " does not exist");
            }
            logger.fine("Deleting book with id: " + id);
            bookRepository.deleteById(id);
            logger.info("Book deleted successfully with id: " + id);
            return StandardResponse.success("Book deleted successfully", true);
        } catch (Exception e) {
            logger.severe("Error deleting book: " + e.getMessage());
            return StandardResponse.error("Failed to delete book", e.getMessage());
        }
    }

    private BookDTO convertToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }
}
