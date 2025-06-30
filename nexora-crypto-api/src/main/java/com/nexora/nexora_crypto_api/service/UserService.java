package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.User;

import java.util.List;

public interface UserService {

    List<User> allUsers() throws Exception;
    User getUserById(Long userId);
}
