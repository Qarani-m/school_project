package com.libraryservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "borrowed_books")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long bookId;
    private String userId;
    private boolean status;
}
