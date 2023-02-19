package com.myfancypfdinvoices.service;

import com.myfancypfdinvoices.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User findById(String id) {
        logger.info("finById was invoked with id {}", id);
        String randomName = UUID.randomUUID().toString();
        return new User(id, randomName);
    }
}
