package org.example.library.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "loans")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default private LocalDate loanDate   = LocalDate.now();
    private LocalDate returnDate;
    @Builder.Default private boolean   returned   = false;
    @Builder.Default private Double    fine        = 0.0;
}
