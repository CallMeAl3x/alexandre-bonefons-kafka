package org.example.library.service;

import org.example.library.dto.BookDTO;
import java.util.List;

public interface BookService {
    List<BookDTO> findAll();
    BookDTO findById(Long id);
    BookDTO create(BookDTO dto);
    BookDTO update(Long id, BookDTO dto);
    void delete(Long id);
    List<BookDTO> findAvailable();
    List<BookDTO> findByGenre(String genre);
}
