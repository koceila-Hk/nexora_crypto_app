package com.nexora.nexora_crypto_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nexora.nexora_crypto_api.model.Token;
import com.nexora.nexora_crypto_api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    private User user;
    private Token token1;
    private Token token2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        token1 = new Token();
        token1.setId(100L);
        token1.setUser(user);
        token1.setToken("token1");
        token1.setExpired(false);
        token1.setRevoked(false);

        token2 = new Token();
        token2.setId(101L);
        token2.setUser(user);
        token2.setToken("token2");
        token2.setExpired(true);
        token2.setRevoked(false);
    }

    @Test
    void findAllValidTokenByUser_shouldReturnOnlyValidTokens() {
        when(tokenRepository.findAllValidTokenByUser(1L)).thenReturn(Arrays.asList(token1, token2));

        List<Token> result = tokenRepository.findAllValidTokenByUser(1L);

        assertThat(result).hasSize(2);
        assertThat(result).contains(token1, token2);
        assertThat(result.get(0).getUser().getId()).isEqualTo(1L);
    }

    @Test
    void findAllValidTokenByUser_shouldReturnEmpty_whenNoValidTokens() {
        when(tokenRepository.findAllValidTokenByUser(2L)).thenReturn(Collections.emptyList());

        List<Token> result = tokenRepository.findAllValidTokenByUser(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByToken_shouldReturnToken_whenExists() {
        when(tokenRepository.findByToken("token1")).thenReturn(Optional.of(token1));

        Optional<Token> result = tokenRepository.findByToken("token1");

        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo("token1");
        assertThat(result.get().getUser().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void findByToken_shouldReturnEmpty_whenNotExists() {
        when(tokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());

        Optional<Token> result = tokenRepository.findByToken("nonexistent");

        assertThat(result).isEmpty();
    }
}
