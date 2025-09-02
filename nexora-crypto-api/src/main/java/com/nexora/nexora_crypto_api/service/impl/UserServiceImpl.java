package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> allUsers() throws Exception {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    /**
     * Récupère un utilisateur à partir de son ID.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return l'utilisateur correspondant
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}