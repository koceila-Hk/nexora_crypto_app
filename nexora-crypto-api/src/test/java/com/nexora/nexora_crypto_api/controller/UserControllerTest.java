package com.nexora.nexora_crypto_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

import com.nexora.nexora_crypto_api.mappers.UserMapper;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.UserDto;
import com.nexora.nexora_crypto_api.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(5000.0));

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setBalance(BigDecimal.valueOf(5000.0));
    }

    @Test
    void getBalance_shouldReturnUserDto_whenUserExists() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.getBalance(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(5000.0));
    }

    @Test
    void getBalance_shouldReturnNotFound_whenUserDoesNotExist() {
        // Arrange
        when(userService.getUserById(2L)).thenReturn(null);

        // Act
        ResponseEntity<UserDto> response = userController.getBalance(2L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}
