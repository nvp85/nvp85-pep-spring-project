package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.entity.*;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.*;;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleDuplicateUsername(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Account> createAccount(@RequestBody Account newAccount) throws UsernameAlreadyExistsException {
        if (newAccount.getUsername().length() > 0 && newAccount.getPassword().length() >= 4) {
            return ResponseEntity.status(400).body(null);
        }

        Account account = accountService.createAccount(newAccount);
        return ResponseEntity.status(200).body(account);
        
    }
}
