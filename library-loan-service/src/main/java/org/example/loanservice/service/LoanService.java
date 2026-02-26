package org.example.loanservice.service;

import lombok.RequiredArgsConstructor;
import org.example.loanservice.client.BookServiceClient;
import org.example.loanservice.client.UserServiceClient;
import org.example.loanservice.dto.*;
import org.example.loanservice.entity.Loan;
import org.example.loanservice.exception.ResourceNotFoundException;
import org.example.loanservice.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int    LOAN_DAYS  = 14;
    private static final double DAILY_FINE = 0.20;

    private final LoanRepository    repo;
    private final BookServiceClient bookClient;
    private final UserServiceClient userClient;

    public List<LoanDTO> findAll() {
        return repo.findAll().stream().map(this::enrich).toList();
    }

    public LoanDTO findById(Long id) {
        return enrich(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", id)));
    }

    /**
     * Creates a loan: validates book & user via REST calls, sets book unavailable.
     */
    public LoanDTO create(Long bookId, Long userId) {
        // 1. Call book-service to validate & check availability
        BookDTO book = bookClient.getBook(bookId);
        if (!book.isAvailable())
            throw new IllegalArgumentException("Book is not available: " + bookId);

        // 2. Call user-service to validate user exists
        userClient.getUser(userId);

        // 3. Mark book as unavailable via book-service
        bookClient.setAvailability(bookId, false);

        // 4. Persist loan locally
        Loan loan = Loan.builder()
                .bookId(bookId).userId(userId)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(LOAN_DAYS))
                .build();
        Loan saved = repo.save(loan);

        // 5. Build response with enriched data
        return LoanDTO.builder()
                .id(saved.getId())
                .bookId(bookId).bookTitle(book.getTitle())
                .userId(userId).username(userClient.getUser(userId).getUsername())
                .loanDate(saved.getLoanDate()).returnDate(saved.getReturnDate())
                .returned(false).fine(0.0).build();
    }

    public LoanDTO returnBook(Long loanId) {
        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", loanId));
        if (loan.isReturned())
            throw new IllegalArgumentException("Loan already returned: " + loanId);

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        long overdue = ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(LOAN_DAYS), LocalDate.now());
        if (overdue > 0) loan.setFine(overdue * DAILY_FINE);

        // Mark book available again via book-service
        bookClient.setAvailability(loan.getBookId(), true);

        return enrich(repo.save(loan));
    }

    public List<LoanDTO> findByUser(Long userId) {
        return repo.findByUserId(userId).stream().map(this::enrich).toList();
    }

    public List<LoanDTO> findActive() {
        return repo.findByReturnedFalse().stream().map(this::enrich).toList();
    }

    // ---- enrichment: fill bookTitle & username from remote services ----

    private LoanDTO enrich(Loan l) {
        String bookTitle = "Unknown";
        String username  = "Unknown";
        try { bookTitle = bookClient.getBook(l.getBookId()).getTitle(); } catch (Exception ignored) {}
        try { username  = userClient.getUser(l.getUserId()).getUsername(); } catch (Exception ignored) {}

        return LoanDTO.builder()
                .id(l.getId())
                .bookId(l.getBookId()).bookTitle(bookTitle)
                .userId(l.getUserId()).username(username)
                .loanDate(l.getLoanDate()).returnDate(l.getReturnDate())
                .returned(l.isReturned()).fine(l.getFine()).build();
    }
}
