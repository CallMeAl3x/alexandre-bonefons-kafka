package org.example.library.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDTO {
    private Long          id;
    private Long          bookId;
    private String        bookTitle;
    private Long          userId;
    private String        username;
    private Integer       rating;
    private String        comment;
    private LocalDateTime createdAt;
}
