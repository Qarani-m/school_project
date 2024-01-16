package com.libraryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "books")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long bookId; // 86234
    private String title; // Made Familiar Mathematics
    private String level; //one, two, three, four (form)
    private String genre; // science, math etc
    private boolean available; //true, false
}
