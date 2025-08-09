package com.nexora.nexora_crypto_api.mappers;

import com.nexora.nexora_crypto_api.model.dto.UserDto;
import com.nexora.nexora_crypto_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setBalance(user.getBalance());
        return dto;
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setBalance(dto.getBalance());
        return user;
    }
}
