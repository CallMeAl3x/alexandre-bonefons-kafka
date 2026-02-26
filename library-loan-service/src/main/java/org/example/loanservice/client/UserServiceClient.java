package org.example.loanservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loanservice.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * REST client that calls library-user-service via Eureka (load-balanced).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private static final String BASE = "http://library-user-service/users";

    public UserDTO getUser(Long userId) {
        log.info("→ GET {}/{}", BASE, userId);
        return restTemplate.getForObject(BASE + "/" + userId, UserDTO.class);
    }
}
