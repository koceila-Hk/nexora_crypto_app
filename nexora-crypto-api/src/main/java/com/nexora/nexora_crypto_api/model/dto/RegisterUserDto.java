package com.nexora.nexora_crypto_api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String pseudonym;
    private String email;
    private String password;
}