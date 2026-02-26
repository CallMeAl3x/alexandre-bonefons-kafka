package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.LoanDTO;
import org.example.library.exception.InvalidBookException;
import org.example.library.exception.ResourceNotFoundException;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.LoanService;
import org.example.library.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service @RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private static final int    LOAN_DAYS       = 14;
    private static final double DAILY_FINE      = 0.20;

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final DTOMapper      mapper;

    @Override public List<LoanDTO> findAll() {
        return loanRepo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public LoanDTO findById(Long id) {
        return mapper.toDto(loanRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", id)));
    }

    @Override public LoanDTO create(Long bookId, Long userId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
        if (!book.isAvailable())
            throw new InvalidBookException("Book is not available: " + bookId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        book.setAvailable(false);
        bookRepo.save(book);

        Loan loan = Loan.builder().book(book).user(user)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(LOAN_DAYS))
                .build();
        return mapper.toDto(loanRepo.save(loan));
    }

    @Override public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", loanId));
        if (loan.isReturned())
            throw new IllegalArgumentException("Loan already returned: " + loanId);

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        long overdue = ChronoUnit.DAYS.between(LocalDate.now(), loan.getReturnDate());
        if (overdue < 0) loan.setFine(Math.abs(overdue) * DAILY_FINE);

        loan.getBook().setAvailable(true);
        bookRepo.save(loan.getBook());
        return mapper.toDto(loanRepo.save(loan));
    }

    @Override public List<LoanDTO> findByUser(Long userId) {
        return loanRepo.findByUserId(userId).stream().map(mapper::toDto).toList();
    }

    @Override public List<LoanDTO> findActive() {
        return loanRepo.findByReturnedFalse().stream().map(mapper::toDto).toList();
    }
}
