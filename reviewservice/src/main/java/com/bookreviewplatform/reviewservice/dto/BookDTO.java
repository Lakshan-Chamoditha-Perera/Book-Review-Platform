package com.bookreviewplatform.reviewservice.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {
    private UUID id;
    private String title;
    private String author;
}