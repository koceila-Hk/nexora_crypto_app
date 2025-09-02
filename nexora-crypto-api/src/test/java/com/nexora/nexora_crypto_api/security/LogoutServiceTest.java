package com.nexora.nexora_crypto_api.security;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nexora.nexora_crypto_api.model.Token;
import com.nexora.nexora_crypto_api.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

class LogoutServiceTest {

    private LogoutService logoutService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logoutService = new LogoutService(tokenRepository);
    }


    @Test
    void logout_shouldExpireAndRevokeToken_whenTokenExists() {
        String jwt = "Bearer abc123";
        Token storedToken = new Token();
        storedToken.setToken("abc123");
        storedToken.setExpired(false);
        storedToken.setRevoked(false);

        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(tokenRepository.findByToken("abc123")).thenReturn(Optional.of(storedToken));
        when(tokenRepository.save(storedToken)).thenReturn(storedToken);

        logoutService.logout(request, response, authentication);

        assertThat(storedToken.isExpired()).isTrue();
        assertThat(storedToken.isRevoked()).isTrue();
        verify(tokenRepository).save(storedToken);
    }

    @Test
    void logout_shouldDoNothing_whenTokenNotFound() {
        String jwt = "Bearer abc123";

        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(tokenRepository.findByToken("abc123")).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any());
    }
}
