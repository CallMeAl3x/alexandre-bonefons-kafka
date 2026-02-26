package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.ReviewDTO;
import org.example.library.exception.ResourceNotFoundException;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.ReviewService;
import org.example.library.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final BookRepository   bookRepo;
    private final UserRepository   userRepo;
    private final DTOMapper        mapper;

    @Override public List<ReviewDTO> findAll() {
        return reviewRepo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public ReviewDTO findById(Long id) {
        return mapper.toDto(reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", id)));
    }

    @Override public ReviewDTO create(Long bookId, Long userId, Integer rating, String comment) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return mapper.toDto(reviewRepo.save(
                Review.builder().book(book).user(user).rating(rating).comment(comment).build()));
    }

    @Override public List<ReviewDTO> findByBook(Long bookId) {
        return reviewRepo.findByBookId(bookId).stream().map(mapper::toDto).toList();
    }

    @Override public Double averageRating(Long bookId) {
        return reviewRepo.averageRatingByBookId(bookId);
    }

    @Override public void delete(Long id) {
        if (!reviewRepo.existsById(id)) throw new ResourceNotFoundException("Review", id);
        reviewRepo.deleteById(id);
    }
}
