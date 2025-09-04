package com.nexora.nexora_crypto_api.mappers;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.UserDto;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @AfterEach
    void tearDown() {
        userMapper = null;
    }

    @Test
    void toDto_shouldMapUserToUserDto() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setBalance(BigDecimal.valueOf(100.0));

        // Act
        UserDto dto = userMapper.toDto(user);

        // Assert
        assertNotNull(dto);
        assertEquals(123L, dto.getId());
        assertEquals(BigDecimal.valueOf(100.0), dto.getBalance());
    }

    @Test
    void toEntity_shouldMapDtoToUser() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setId(456L);
        dto.setBalance(BigDecimal.valueOf(200.0));

        // Act
        User user = userMapper.toEntity(dto);

        // Assert
        assertNotNull(user);
        assertEquals(456L, user.getId());
        assertEquals(BigDecimal.valueOf(200.0), user.getBalance());
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        // Act
        User user = userMapper.toEntity(null);

        // Assert
        assertNull(user);
    }
}
