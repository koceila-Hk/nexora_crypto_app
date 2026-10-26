package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.UserDto;
import com.nexora.nexora_crypto_api.mappers.UserMapper;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = (User) authentication.getPrincipal();
        UserDto userDto = userMapper.toDto(currentUser);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<UserDto> getBalance(@PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        // Un utilisateur ne peut consulter que son propre solde
        if (currentUser == null || !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
}