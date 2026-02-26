package org.example.loanservice.repository;

import org.example.loanservice.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByBookId(Long bookId);
    List<Loan> findByReturnedFalse();
    boolean existsByBookIdAndReturnedFalse(Long bookId);
}
