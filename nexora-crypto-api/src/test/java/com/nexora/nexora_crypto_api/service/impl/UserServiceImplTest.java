package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("koko", "koko@gmail.com", "123456class"));
        userRepository.save(new User("kouss", "kokos@gmail.com", "123456class"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void allUsers() throws Exception {
        List<User> users = userService.allUsers();
        Assertions.assertEquals(2, users.size());
        Assertions.assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("koko@gmail.com")));
    }
}