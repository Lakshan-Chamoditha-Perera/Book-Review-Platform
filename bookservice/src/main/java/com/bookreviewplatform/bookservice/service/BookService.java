package com.bookreviewplatform.bookservice.service;

import com.bookreviewplatform.bookservice.dto.BookDTO;
import com.bookreviewplatform.bookservice.dto.BookRequestDTO;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(UUID id);
    BookDTO saveBook(BookRequestDTO bookRequestDTO);
    BookDTO updateBook(UUID id, BookRequestDTO bookRequestDTO);
    Boolean deleteBook(UUID id);
}
