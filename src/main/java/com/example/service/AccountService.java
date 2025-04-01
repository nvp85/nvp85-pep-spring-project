package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.*;
import com.example.exception.*;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) throws UsernameAlreadyExistsException{
        Account sameUsername = accountRepository.findAccountByUsername(account.getUsername());
        if (sameUsername != null) {
            throw new UsernameAlreadyExistsException();
        }
        return accountRepository.save(account);
    }

    public Account loginAccount(Account credentials) throws InvalidCredentialsException{
        Account account = accountRepository.findAccountByUsername(credentials.getUsername());
        if (account == null || !account.getPassword().equals(credentials.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return account;
    }
}
