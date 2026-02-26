package org.example.loanservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loanservice.dto.BookDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * REST client that calls library-book-service via Eureka (load-balanced).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceClient {

    private final RestTemplate restTemplate;
    private static final String BASE = "http://library-book-service/books";

    public BookDTO getBook(Long bookId) {
        log.info("→ GET {}/{}", BASE, bookId);
        return restTemplate.getForObject(BASE + "/" + bookId, BookDTO.class);
    }

    public void setAvailability(Long bookId, boolean available) {
        log.info("→ PATCH {}/{}/availability  available={}", BASE, bookId, available);
        restTemplate.patchForObject(
                BASE + "/" + bookId + "/availability",
                Map.of("available", available),
                BookDTO.class);
    }
}
