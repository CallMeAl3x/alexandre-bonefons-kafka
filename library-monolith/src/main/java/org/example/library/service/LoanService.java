package org.example.library.service;

import org.example.library.dto.LoanDTO;
import java.util.List;

public interface LoanService {
    List<LoanDTO> findAll();
    LoanDTO findById(Long id);
    LoanDTO create(Long bookId, Long userId);
    LoanDTO returnBook(Long loanId);
    List<LoanDTO> findByUser(Long userId);
    List<LoanDTO> findActive();
}
