package org.example.bookservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.BookDTO;
import org.example.bookservice.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping
    public List<BookDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/available")
    public List<BookDTO> getAvailable() { return service.findAvailable(); }

    @GetMapping("/genre/{genre}")
    public List<BookDTO> getByGenre(@PathVariable String genre) { return service.findByGenre(genre); }

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Internal endpoint called by loan-service */
    @PatchMapping("/{id}/availability")
    public BookDTO setAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return service.setAvailability(id, body.get("available"));
    }
}
