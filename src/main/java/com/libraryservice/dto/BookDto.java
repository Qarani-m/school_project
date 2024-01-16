package com.libraryservice.dto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String title; // Made Familiar Mathematics
    private String level; //one, two, three, four (form)
    private String genre; // science, math etc
    private boolean available; //true, false
}
