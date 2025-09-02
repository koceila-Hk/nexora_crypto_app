package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void allUsers_shouldReturnListOfUsers() throws Exception {
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.allUsers();

        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserIfExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("user@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowExceptionIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }
}
