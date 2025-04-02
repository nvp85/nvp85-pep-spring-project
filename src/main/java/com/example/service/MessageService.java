package com.example.service;

import org.springframework.stereotype.Service;
import com.example.entity.*;
import com.example.repository.*;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

}
