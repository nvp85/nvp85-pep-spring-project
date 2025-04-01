package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.*;
import com.example.exception.UsernameAlreadyExistsException;
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

}
