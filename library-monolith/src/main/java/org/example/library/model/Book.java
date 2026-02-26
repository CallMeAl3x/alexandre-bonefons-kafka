package org.example.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)          private String  title;
    @Column(nullable = false)          private String  author;
    @Column(unique = true)             private String  isbn;
    private String  genre;
    private Integer pages;
    private Double  price;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean available = true;
}
