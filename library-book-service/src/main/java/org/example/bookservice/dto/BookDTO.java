package org.example.bookservice.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookDTO {
    private Long    id;
    private String  title;
    private String  author;
    private String  isbn;
    private String  genre;
    private Integer pages;
    private Double  price;
    private boolean available;
}
