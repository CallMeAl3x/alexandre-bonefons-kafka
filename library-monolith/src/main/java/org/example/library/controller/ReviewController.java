package org.example.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.ReviewDTO;
import org.example.library.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @GetMapping
    public List<ReviewDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ReviewDTO getById(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/book/{bookId}")
    public List<ReviewDTO> getByBook(@PathVariable Long bookId) { return service.findByBook(bookId); }

    @GetMapping("/book/{bookId}/average")
    public Map<String, Object> getAverage(@PathVariable Long bookId) {
        return Map.of("bookId", bookId, "averageRating",
                service.averageRating(bookId) != null ? service.averageRating(bookId) : 0.0);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(
                Long.parseLong(body.get("bookId").toString()),
                Long.parseLong(body.get("userId").toString()),
                Integer.parseInt(body.get("rating").toString()),
                (String) body.get("comment")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
