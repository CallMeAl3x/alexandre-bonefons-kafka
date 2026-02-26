package org.example.loanservice.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanDTO {
    private Long      id;
    private Long      bookId;
    private String    bookTitle;
    private Long      userId;
    private String    username;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean   returned;
    private Double    fine;
}
