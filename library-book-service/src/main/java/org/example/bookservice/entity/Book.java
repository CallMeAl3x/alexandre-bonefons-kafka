package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    private String genre;
    private Integer pages;
    private Double price;

    @Builder.Default
    @Column(nullable = false)
    private boolean available = true;
}
