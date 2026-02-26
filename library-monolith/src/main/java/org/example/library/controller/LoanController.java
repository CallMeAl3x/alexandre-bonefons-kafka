package org.example.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.LoanDTO;
import org.example.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @GetMapping
    public List<LoanDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public LoanDTO getById(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/active")
    public List<LoanDTO> getActive() { return service.findActive(); }

    @GetMapping("/user/{userId}")
    public List<LoanDTO> getByUser(@PathVariable Long userId) { return service.findByUser(userId); }

    @PostMapping
    public ResponseEntity<LoanDTO> create(@RequestBody Map<String, Long> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(body.get("bookId"), body.get("userId")));
    }

    @PutMapping("/{id}/return")
    public LoanDTO returnBook(@PathVariable Long id) { return service.returnBook(id); }
}
