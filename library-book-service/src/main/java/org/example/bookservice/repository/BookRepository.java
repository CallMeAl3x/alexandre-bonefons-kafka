package org.example.bookservice.repository;

import org.example.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAvailableTrue();
    List<Book> findByGenre(String genre);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    Optional<Book> findByIsbn(String isbn);
}
