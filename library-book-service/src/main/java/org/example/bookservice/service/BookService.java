package org.example.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.BookDTO;
import org.example.bookservice.entity.Book;
import org.example.bookservice.exception.ResourceNotFoundException;
import org.example.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repo;

    public List<BookDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public BookDTO findById(Long id) {
        return toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id)));
    }

    public BookDTO create(BookDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new IllegalArgumentException("Title is required");
        if (dto.getAuthor() == null || dto.getAuthor().isBlank())
            throw new IllegalArgumentException("Author is required");
        if (dto.getIsbn() != null && repo.findByIsbn(dto.getIsbn()).isPresent())
            throw new IllegalArgumentException("ISBN already exists: " + dto.getIsbn());
        Book book = toEntity(dto);
        book.setId(null);
        return toDto(repo.save(book));
    }

    public BookDTO update(Long id, BookDTO dto) {
        Book b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        if (dto.getTitle()  != null) b.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) b.setAuthor(dto.getAuthor());
        if (dto.getIsbn()   != null) b.setIsbn(dto.getIsbn());
        if (dto.getGenre()  != null) b.setGenre(dto.getGenre());
        if (dto.getPages()  != null) b.setPages(dto.getPages());
        if (dto.getPrice()  != null) b.setPrice(dto.getPrice());
        b.setAvailable(dto.isAvailable());
        return toDto(repo.save(b));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Book", id);
        repo.deleteById(id);
    }

    public List<BookDTO> findAvailable() {
        return repo.findByAvailableTrue().stream().map(this::toDto).toList();
    }

    public List<BookDTO> findByGenre(String genre) {
        return repo.findByGenre(genre).stream().map(this::toDto).toList();
    }

    /** Called by loan-service to mark a book unavailable / available */
    public BookDTO setAvailability(Long id, boolean available) {
        Book b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        b.setAvailable(available);
        return toDto(repo.save(b));
    }

    // ---- mapping helpers ----

    private BookDTO toDto(Book b) {
        return BookDTO.builder()
                .id(b.getId()).title(b.getTitle()).author(b.getAuthor())
                .isbn(b.getIsbn()).genre(b.getGenre()).pages(b.getPages())
                .price(b.getPrice()).available(b.isAvailable()).build();
    }

    private Book toEntity(BookDTO d) {
        return Book.builder()
                .id(d.getId()).title(d.getTitle()).author(d.getAuthor())
                .isbn(d.getIsbn()).genre(d.getGenre()).pages(d.getPages())
                .price(d.getPrice()).available(d.isAvailable()).build();
    }
}
