package com.bookreviewplatform.bookservice.service;

import com.bookreviewplatform.bookservice.dto.BookRequestDTO;
import com.bookreviewplatform.bookservice.payloads.StandardResponse;

import java.util.UUID;

public interface BookService {
    StandardResponse getAllBooks();
    StandardResponse getBookById(UUID id);
    StandardResponse saveBook(BookRequestDTO bookRequestDTO);
    StandardResponse updateBook(UUID id, BookRequestDTO bookRequestDTO);
    StandardResponse deleteBook(UUID id);
}
