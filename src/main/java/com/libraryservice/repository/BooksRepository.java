package com.libraryservice.repository;

import com.libraryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleAndLevelAndGenre(String title, String level, String genre);
    List<Book> findByAvailableTrue();

}
