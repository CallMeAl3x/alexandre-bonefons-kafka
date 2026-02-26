package org.example.library.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String    username;
    @Column(nullable = false, unique = true) private String    email;
    private String    firstName;
    private String    lastName;
    @Builder.Default
    private LocalDate membershipDate = LocalDate.now();
}
