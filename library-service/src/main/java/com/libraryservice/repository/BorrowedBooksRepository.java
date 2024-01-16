package com.libraryservice.repository;

import com.libraryservice.entity.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooks, Long> {
    BorrowedBooks findByBookId(Long bookId );
    List findByUserIdAndStatus(String userId, boolean status);
}
