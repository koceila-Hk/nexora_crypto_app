package com.nexora.nexora_crypto_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @NotBlank
    private String pseudonym;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}