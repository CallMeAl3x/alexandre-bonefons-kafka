package org.example.accountservice.service;

import org.example.accountservice.dto.AccountDetailsDTO;
import org.example.accountservice.entity.Account;

import java.util.List;

public interface AccountService {
    public List<Account> getAllAccounts();

    public Account getAccountById(Long id);

    public Account saveAccount(Account account);

    public void deleteAccount(Long id);

    public AccountDetailsDTO getAccountDetails(Long accountId);
}