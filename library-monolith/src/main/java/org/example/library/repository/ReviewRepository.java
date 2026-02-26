package org.example.library.repository;

import org.example.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByUserId(Long userId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double averageRatingByBookId(Long bookId);
}
