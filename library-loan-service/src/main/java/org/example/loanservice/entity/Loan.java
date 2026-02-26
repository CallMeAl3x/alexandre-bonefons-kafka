package org.example.loanservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "library_loans")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** References book-service — stored as foreign key ID only (no @ManyToOne across services) */
    @Column(nullable = false)
    private Long bookId;

    /** References user-service */
    @Column(nullable = false)
    private Long userId;

    @Builder.Default
    private LocalDate loanDate = LocalDate.now();

    private LocalDate returnDate;

    @Builder.Default
    private boolean returned = false;

    @Builder.Default
    private Double fine = 0.0;
}
