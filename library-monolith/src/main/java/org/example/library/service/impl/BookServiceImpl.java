package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.BookDTO;
import org.example.library.exception.InvalidBookException;
import org.example.library.exception.ResourceNotFoundException;
import org.example.library.model.Book;
import org.example.library.repository.BookRepository;
import org.example.library.service.BookService;
import org.example.library.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repo;
    private final DTOMapper mapper;

    @Override public List<BookDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public BookDTO findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id)));
    }

    @Override public BookDTO create(BookDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new InvalidBookException("Title is required");
        if (dto.getAuthor() == null || dto.getAuthor().isBlank())
            throw new InvalidBookException("Author is required");
        if (dto.getIsbn() != null && repo.findByIsbn(dto.getIsbn()).isPresent())
            throw new InvalidBookException("ISBN already exists: " + dto.getIsbn());
        Book book = mapper.toEntity(dto);
        book.setId(null);
        return mapper.toDto(repo.save(book));
    }

    @Override public BookDTO update(Long id, BookDTO dto) {
        Book existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        if (dto.getTitle()  != null) existing.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) existing.setAuthor(dto.getAuthor());
        if (dto.getIsbn()   != null) existing.setIsbn(dto.getIsbn());
        if (dto.getGenre()  != null) existing.setGenre(dto.getGenre());
        if (dto.getPages()  != null) existing.setPages(dto.getPages());
        if (dto.getPrice()  != null) existing.setPrice(dto.getPrice());
        existing.setAvailable(dto.isAvailable());
        return mapper.toDto(repo.save(existing));
    }

    @Override public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Book", id);
        repo.deleteById(id);
    }

    @Override public List<BookDTO> findAvailable() {
        return repo.findByAvailableTrue().stream().map(mapper::toDto).toList();
    }

    @Override public List<BookDTO> findByGenre(String genre) {
        return repo.findByGenre(genre).stream().map(mapper::toDto).toList();
    }
}
