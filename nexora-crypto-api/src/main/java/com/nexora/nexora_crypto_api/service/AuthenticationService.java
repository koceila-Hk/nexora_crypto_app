package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    void signup(RegisterUserDto input) throws Exception;
    User authenticate(LoginUserDto input) throws Exception;

    void verifyUser(VerifyUserDto input) throws Exception;
    void resendVerificationCode(String email) throws Exception;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void saveUserToken(User user, String jwtToken);
    void revokeAllUserTokens(User user);
}
