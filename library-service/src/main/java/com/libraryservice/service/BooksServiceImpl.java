package com.libraryservice.service;

import com.libraryservice.dto.BookDto;
import com.libraryservice.entity.Book;
import com.libraryservice.repository.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {
    Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private BooksRepository booksRepository;

    @Override
    public Object getBookById(Long bookId) {
        try {
            Optional<Book> optionalBook = booksRepository.findById(bookId);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                logger.info("Book retrieved successfully with ID: {}", bookId);
                return ResponseEntity.ok(book);
            } else {
                logger.info("No book with id: {}", bookId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with ID: " + bookId);
            }
        } catch (Exception e) {
            logger.error("Error while retrieving book with ID: {}", bookId, e);
            throw new RuntimeException("Error while retrieving book with ID: " + bookId, e);
        }
    }

    @Override
    public Object getAllBooks() {
        try {
            List<Book> books = booksRepository.findAll();
            if (!books.isEmpty()) {
                logger.info("All books retrieved successfully");
                return ResponseEntity.ok(books);
            } else {
                logger.info("No books found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books found");
            }
        } catch (Exception e) {
            logger.error("Error while retrieving all books", e);
            throw new RuntimeException("Error while retrieving all books", e);
        }

    }

    @Override
    public Object addBook(BookDto bookDto) {
        try {
            Book bookToAdd = Book.builder()
                    .title(bookDto.getTitle())
                    .available(true)
                    .level(bookDto.getLevel())
                    .genre(bookDto.getGenre())
                    .build();
            Book addedBook = booksRepository.save(bookToAdd);
            logger.info("Book added successfully with ID: {}", addedBook.getBookId());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
        } catch (Exception e) {
            logger.error("Error while adding a book", e);
            throw new RuntimeException("Error while adding a book", e);
        }
    }

    @Override
    public Object updateBook(Long bookId, BookDto updatedBookDto) {
        try {
            return booksRepository.findById(bookId)
                    .map(existingBook -> {
                        existingBook.setGenre(updatedBookDto.getGenre());
                        existingBook.setAvailable(updatedBookDto.isAvailable());
                        existingBook.setLevel(updatedBookDto.getLevel());
                        Book updatedBook = booksRepository.save(existingBook);
                        logger.info("Book updated successfully with ID: {}", bookId);
                        return ResponseEntity.ok(updatedBook); // Wrap the updatedBook in ResponseEntity
                    })
                    .orElseGet(() -> {
                        String errorMessage = "No book found with ID: " + bookId;
                        logger.info(errorMessage);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Book());
                    });
        } catch (Exception e) {
            logger.error("Error while updating book with ID: {}", bookId, e);
            throw new RuntimeException("Error while updating book with ID: " + bookId, e);
        }
    }


    @Override
    public Object deleteBook(Long bookId) {
        try {
            return booksRepository.findById(bookId)
                    .map(existingBook -> {
                        booksRepository.delete(existingBook);
                        logger.info("Book deleted successfully with ID: {}", bookId);
                        return ResponseEntity.noContent().build();
                    })
                    .orElseGet(() -> {
                        String errorMessage = "No book found with ID: " + bookId;
                        logger.info(errorMessage);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
                    });
        } catch (Exception e) {
            logger.error("Error while deleting book with ID: {}", bookId, e);
            throw new RuntimeException("Error while deleting book with ID: " + bookId, e);
        }

    }


    @Override
    public Object getAvailableBooks() {
        try {
            List<Book> availableBooks = booksRepository.findByAvailableTrue();

            if (availableBooks.isEmpty()) {
                logger.info("No available books found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available books found.");
            } else {
                logger.info("Available books retrieved successfully.");
                return ResponseEntity.ok(availableBooks);
            }
        } catch (Exception e) {
            logger.error("Error while retrieving available books.", e);
            throw new RuntimeException("Error while retrieving available books.", e);
        }
    }

    @Override
    public Object searchBooks(String title, String level, String genre) {
        try {
            List<Book> searchResults = booksRepository.findByTitleAndLevelAndGenre(title, level, genre);
            if (searchResults.isEmpty()) {
                logger.info("No books found with Title: {}, Level: {}, Genre: {}", title, level, genre);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books found with provided criteria");
            } else {
                logger.info("Books searched successfully with Title: {}, Level: {}, Genre: {}", title, level, genre);
                return ResponseEntity.ok(searchResults);
            }
        } catch (Exception e) {
            logger.error("Error while searching books with Title: {}, Level: {}, Genre: {}", title, level, genre, e);
            throw new RuntimeException("Error while searching books with Title: " + title +
                    ", Level: " + level + ", Genre: " + genre, e);
        }
    }
}
