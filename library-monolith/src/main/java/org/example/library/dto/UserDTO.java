package org.example.library.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long      id;
    private String    username;
    private String    email;
    private String    firstName;
    private String    lastName;
    private LocalDate membershipDate;
}
