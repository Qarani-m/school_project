package com.libraryservice.controller;

import com.libraryservice.dto.BorrowRequest;
import com.libraryservice.dto.ReturnRequest;
import com.libraryservice.entity.Book;
import com.libraryservice.exceptions.BookNotFoundException;
import com.libraryservice.service.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lib/")
public class OperationsController {

    @Autowired
    private OperationsService operationsService;
    @PostMapping("/books/{bookId}/borrow")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, @RequestBody BorrowRequest request) {
        Object result = operationsService.borrowBook(bookId, request.getUserId());
        if (result instanceof ResponseEntity) {
            return (ResponseEntity<Void>) result;
        } else {
            return ResponseEntity.ok().build();
        }
    }

    // Return a borrowed copy of a book
    @PostMapping("/books/{bookId}/return")
    public ResponseEntity<Object> returnBook(@PathVariable Long bookId, @RequestBody ReturnRequest request) {
        try {
            operationsService.returnBook(bookId, request.getUserId());
            return ResponseEntity.ok("Book with ID " + bookId + " returned successfully by User ID: " + request.getUserId());
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(404).body("Book not found with ID: " + bookId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while returning book with ID: " + bookId);
        }
    }

    // Get a list of books currently borrowed by a specific library user
    @GetMapping("/users/{userID}/borrowed-books")
    public ResponseEntity<List<Book>> getBorrowedBooksForUser(@PathVariable String userId) {
        List<Book> borrowedBooks = operationsService.getBorrowedBooksForUser(userId);
        return ResponseEntity.ok(borrowedBooks);
    }
}
