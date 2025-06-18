package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.model.User;

public interface AuthenticationService {
    void signup(RegisterUserDto input) throws Exception;
    User authenticate(LoginUserDto input) throws Exception;

    void verifyUser(VerifyUserDto input) throws Exception;
    void resendVerificationCode(String email) throws Exception;
}
