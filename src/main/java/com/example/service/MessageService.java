package com.example.service;

import org.springframework.stereotype.Service;
import com.example.entity.*;
import com.example.repository.*;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message newMessage) {
        Optional<Account> postedBy = accountRepository.findById(newMessage.getPostedBy());
        if (!postedBy.isPresent()) {
            throw new EntityNotFoundException();
        }
        return messageRepository.save(newMessage);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    public int deleteMessageById(int id) {
        return messageRepository.deleteById(id);
    }

    public int updateMessageById(int id, String text) {
        int affectedRows = messageRepository.updateMessageTextById(id, text);
        if (affectedRows == 0) {
            throw new EntityNotFoundException();
        }
        return affectedRows;
    }

}
