package org.example.library.service;

import org.example.library.dto.ReviewDTO;
import java.util.List;

public interface ReviewService {
    List<ReviewDTO> findAll();
    ReviewDTO findById(Long id);
    ReviewDTO create(Long bookId, Long userId, Integer rating, String comment);
    List<ReviewDTO> findByBook(Long bookId);
    Double averageRating(Long bookId);
    void delete(Long id);
}
