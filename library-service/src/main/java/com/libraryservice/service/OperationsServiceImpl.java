package com.libraryservice.service;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.BorrowedBooks;
import com.libraryservice.repository.BooksRepository;
import com.libraryservice.repository.BorrowedBooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperationsServiceImpl implements OperationsService {
    Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;
    @Autowired
    private BooksRepository booksRepository;


    @Override
    public Object borrowBook(Long bookId, String userId) {
        try {
            Optional<Book> optionalBook = booksRepository.findById(bookId);
            if (optionalBook.isEmpty()) {
                logger.info("Book with ID {} is not found.", bookId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with ID: " + bookId);
            }
            if (!optionalBook.get().isAvailable()) {
                logger.info("Book with ID {} is not available for borrowing.", bookId);
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book is not available for borrowing.");
            }
            logger.info("Borrowing book with ID {} by User ID: {}", bookId, userId);
            BorrowedBooks borrowedBooks = BorrowedBooks.builder()
                    .bookId(bookId)
                    .userId(userId)
                    .status(true)
                    .build();
            Book book = optionalBook.get();
            book.setAvailable(false);
            booksRepository.save(book);
            borrowedBooksRepository.save(borrowedBooks);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error while borrowing book with ID: {} by User ID: {}", bookId, userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while borrowing book.");
        }
    }


    @Override
    public Object returnBook(Long bookId, String userId) {
        try {
            Optional<BorrowedBooks> optionalBorrowedBook = Optional.ofNullable(borrowedBooksRepository.findByBookId(bookId));
            if (optionalBorrowedBook.isEmpty()) {
                logger.info("No record found for book with ID {}", bookId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record found for the borrowed book.");
            }
            BorrowedBooks borrowedBook = optionalBorrowedBook.get();
            if (!borrowedBook.getUserId().equals(userId)) {
                logger.info("User ID {} does not match the borrower of book with ID {}", userId, bookId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User ID does not match the borrower of the book.");
            }
            borrowedBook.setStatus(false);
            borrowedBooksRepository.save(borrowedBook);

            Optional<Book> optionalBook = booksRepository.findById(bookId);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                book.setAvailable(true);
                booksRepository.save(book);
            } else {
                logger.error("Book with ID {} not found while returning by User ID: {}", bookId, userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found while returning.");
            }
            logger.info("Book with ID {} returned by User ID: {}", bookId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error while returning book with ID: {} by User ID: {}", bookId, userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while returning book.");
        }
    }

    @Override
    public List<Book> getBorrowedBooksForUser(String userId) {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksRepository.findByUserIdAndStatus(userId, true);
        List<Book> borrowedBooks = borrowedBooksList.stream()
                .map(borrowedBook -> {
                    Long bookId = borrowedBook.getBookId();
                    return booksRepository.findById(bookId).orElse(null);
                })
                .filter(book -> book != null)
                .collect(Collectors.toList());
        return borrowedBooks;
    }
}
