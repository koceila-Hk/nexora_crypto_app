package com.nexora.nexora_crypto_api.model.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String pseudonym;
    @Email
    private String email;
    private String password;
}