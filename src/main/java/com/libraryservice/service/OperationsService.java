package com.libraryservice.service;

import com.libraryservice.entity.Book;

import java.util.List;

public interface OperationsService {
    Object borrowBook(Long bookId, String userId);

    Object returnBook(Long bookId, String userId);

    List<Book> getBorrowedBooksForUser(String userId);
}
