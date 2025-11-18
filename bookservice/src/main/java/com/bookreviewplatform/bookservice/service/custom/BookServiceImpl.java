package com.bookreviewplatform.bookservice.service.custom;

import com.bookreviewplatform.bookservice.dto.BookDTO;
import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.entity.Book;
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
    public List<BookDTO> getAllBooks() {
        logger.fine("Fetching all books from database");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.fine("Found " + books.size() + " books in database");
        return books;
    }

    @Override
    public BookDTO getBookById(UUID id) {
        logger.fine("Searching for book with id: " + id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.severe("Book not found with id: " + id);
                    return new RuntimeException("Book not found with id: " + id);
                });
        logger.fine("Book found with id: " + id);
        return convertToDTO(book);
    }



    @Override
    public BookDTO saveBook(BookRequestDTO bookRequestDTO) {
        logger.fine("Creating new book with title: " + bookRequestDTO.getTitle() + 
                    " by author: " + bookRequestDTO.getAuthor());
        
        Book book = Book.builder()
                .title(bookRequestDTO.getTitle())
                .author(bookRequestDTO.getAuthor())
                .build();
        
        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully with id: " + savedBook.getId());
        return convertToDTO(savedBook);
    }

    @Override
    public BookDTO updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        logger.fine("Updating book with id: " + id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.severe("Book not found with id: " + id);
                    return new RuntimeException("Book not found with id: " + id);
                });
        
        logger.fine("Updating book details - Title: " + bookRequestDTO.getTitle() + 
                    ", Author: " + bookRequestDTO.getAuthor());
        book.setTitle(bookRequestDTO.getTitle());
        book.setAuthor(bookRequestDTO.getAuthor());
        
        Book updatedBook = bookRepository.save(book);
        logger.info("Book updated successfully with id: " + id);
        return convertToDTO(updatedBook);
    }

    @Override
    public Boolean deleteBook(UUID id) {
        logger.fine("Checking if book exists with id: " + id);
        if (!bookRepository.existsById(id)) {
            logger.warning("Book not found with id: " + id);
            return false;
        }
        logger.fine("Deleting book with id: " + id);
        bookRepository.deleteById(id);
        logger.info("Book deleted successfully with id: " + id);
        return true;
    }

    private BookDTO convertToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }
}
