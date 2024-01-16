package com.libraryservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReturnRequest {
    private String userId;
}
