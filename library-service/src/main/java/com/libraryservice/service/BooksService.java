package com.libraryservice.service;

import com.libraryservice.dto.BookDto;
import com.libraryservice.entity.Book;

import java.util.List;

public interface BooksService {

    Object getBookById(Long bookId);

    Object getAllBooks();

    Object addBook(BookDto bookDto);

    Object updateBook(Long bookId, BookDto updatedBook);

    Object deleteBook(Long bookId);


    Object getAvailableBooks();

    Object searchBooks(String title, String level, String genre);
}
