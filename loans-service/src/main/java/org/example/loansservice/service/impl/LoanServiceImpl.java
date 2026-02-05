package org.example.loansservice.service.impl;


import org.example.loansservice.entity.Loan;
import org.example.loansservice.repository.LoanRepository;
import org.example.loansservice.rest.AccountServiceClient;
import org.example.loansservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Autowired
    private org.example.loansservice.kafka.LoanKafkaProducer loanKafkaProducer;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public List<Loan> getLoansByAccountId(Long accountId) {
        return loanRepository.findByAccountId(accountId);
    }

    public Loan saveLoan(Loan loan) {
        if (accountServiceClient.accountExists(loan.getAccountId())) {
            Loan savedLoan = loanRepository.save(loan);
            loanKafkaProducer.sendLoanCreated(loan.getAccountId());
            return savedLoan;
        } else {
            throw new RuntimeException("Account does not exist");
        }
    }

    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
        loanRepository.deleteById(id);
        loanKafkaProducer.sendLoanDeleted(loan.getAccountId());
    }

    public void deleteLoanByAccountId(Long accountId) {
        loanRepository.deleteByAccountId(accountId);
    }
}

