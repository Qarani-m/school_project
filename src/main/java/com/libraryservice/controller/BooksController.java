package com.libraryservice.controller;

import com.libraryservice.dto.BookDto;
import com.libraryservice.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lib")

public class BooksController {
    @Autowired
    private BooksService booksService;


    // Retrieve information about a book by its bookId
    @GetMapping("/books/one/{bookId}")
    public ResponseEntity<Object> getBook(@PathVariable Long bookId) {
        Object result = booksService.getBookById(bookId);
        if (result instanceof ResponseEntity) {
            return (ResponseEntity<Object>) result;
        } else {
            // Handle the case where the result is not a ResponseEntity
            return ResponseEntity.ok(result);
        }
    }


    // Get a list of all books in the library
    @GetMapping("/books/all")
    public ResponseEntity<Object> getAllBooks() {
        Object result = booksService.getAllBooks();
        if (result instanceof ResponseEntity) {
            return (ResponseEntity<Object>) result;
        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Add a new book to the library
    @PostMapping("/books/create")
    public ResponseEntity<Object> addBook(@RequestBody BookDto bookDto) {
        try {
            Object addedBook = booksService.addBook(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while adding a book: " + e.getMessage());
        }
    }


    // Update information about a book by its bookId
    @PutMapping("/books/update/{bookId}")
    public ResponseEntity<Object> updateBook(@PathVariable Long bookId, @RequestBody BookDto updatedBook) {
        Object book = booksService.updateBook(bookId, updatedBook);
        return ResponseEntity.ok(book);
    }

    // Delete a book from the library by its bookId
    @DeleteMapping("/books/delete/{bookId}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long bookId) {
        booksService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    // Borrow a specific copy of a book


    // Get a list of books that are currently available for borrowing
    @GetMapping("/books/available")
    public ResponseEntity<Object> getAvailableBooks() {
        Object availableBooksResponse = booksService.getAvailableBooks();
        if (availableBooksResponse instanceof ResponseEntity) {
            return (ResponseEntity<Object>) availableBooksResponse;
        } else {
            return ResponseEntity.ok(availableBooksResponse);
        }
    }
    // Search for books based on parameters like title, author, or genre
    @GetMapping("/books/search")
    public ResponseEntity<Object> searchBooks(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String level,
                                              @RequestParam(required = false) String genre) {
        Object searchResults = booksService.searchBooks(title, level, genre);
        return ResponseEntity.ok(searchResults);
    }
}