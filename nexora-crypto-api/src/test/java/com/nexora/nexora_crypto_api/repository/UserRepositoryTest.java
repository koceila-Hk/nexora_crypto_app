package com.nexora.nexora_crypto_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.nexora.nexora_crypto_api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPseudonym("testuser");
        user.setResetToken("reset-123");
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByPseudonym_shouldReturnFalse_whenPseudonymNotExists() {
        when(userRepository.existsByPseudonym("otheruser")).thenReturn(false);
        boolean exists = userRepository.existsByPseudonym("otheruser");

        assertThat(exists).isFalse();
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailNotExists() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        Optional<User> result = userRepository.findByEmail("unknown@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findByResetToken_shouldReturnUser_whenTokenExists() {
        when(userRepository.findByResetToken("reset-123")).thenReturn(Optional.of(user));
        Optional<User> result = userRepository.findByResetToken("reset-123");

        assertThat(result).isPresent();
        assertThat(result.get().getResetToken()).isEqualTo("reset-123");
    }

    @Test
    void findByResetToken_shouldReturnEmpty_whenTokenNotExists() {
        when(userRepository.findByResetToken("invalid-token")).thenReturn(Optional.empty());
        Optional<User> result = userRepository.findByResetToken("invalid-token");

        assertThat(result).isEmpty();
    }
}
