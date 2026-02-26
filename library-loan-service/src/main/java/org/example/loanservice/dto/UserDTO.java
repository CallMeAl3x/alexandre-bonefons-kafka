package org.example.loanservice.dto;

import lombok.*;
import java.time.LocalDate;

/** Mirror of user-service UserDTO — used by RestTemplate calls */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long      id;
    private String    username;
    private String    email;
    private String    firstName;
    private String    lastName;
    private LocalDate membershipDate;
}
