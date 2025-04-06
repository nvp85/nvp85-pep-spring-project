package com.example.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.entity.*;
import com.example.exception.BadRequestException;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleDuplicateUsername(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Account> createAccount(@RequestBody Account newAccount) throws UsernameAlreadyExistsException, BadRequestException {
        if (newAccount.getUsername().length() == 0 && newAccount.getPassword().length() < 4) {
            throw new BadRequestException("Invalid password or username");
        }
        Account account = accountService.createAccount(newAccount);
        return ResponseEntity.status(200).body(account);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Account> loginAccount(@RequestBody Account credentials) throws InvalidCredentialsException {
        Account account = accountService.loginAccount(credentials);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping("/messages")
    @ResponseBody
    public ResponseEntity<Message> postMessage(@RequestBody Message newMessage) throws BadRequestException {
        if (newMessage.getMessageText().length() == 0 || newMessage.getMessageText().length() > 255) {
            throw new BadRequestException("The message text must not be empty or contain more than 255 characters.");
        }
        Message message = messageService.createMessage(newMessage);
        if (message == null) {
            throw new BadRequestException("Failed to create a new message");
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    @ResponseBody   
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    @ResponseBody
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int affectedRows = messageService.deleteMessageById(messageId);
        if (affectedRows == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(affectedRows);
    } 

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody Message newText) throws BadRequestException{
        if (newText.getMessageText().length() == 0 || newText.getMessageText().length() > 255) {
            throw new BadRequestException("The message text must not be empty or contain more than 255 characters.");
        }
        try {
           int affectedRows = messageService.updateMessageById(messageId, newText.getMessageText());
           return ResponseEntity.status(HttpStatus.OK).body(affectedRows);
        } catch(EntityNotFoundException ex) {
            throw new BadRequestException("The message doesn't exist.");
        }
    }
}
