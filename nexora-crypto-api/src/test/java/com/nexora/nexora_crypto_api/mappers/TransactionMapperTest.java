package com.nexora.nexora_crypto_api.mappers;

import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        transaction1 = new Transaction();
        transaction1.setId(100L);
        transaction1.setUser(user);
        transaction1.setCryptoName("BTC");
        transaction1.setType("BUY");
        transaction1.setQuantity(BigDecimal.valueOf(2.0));
        transaction1.setUnitPrice(BigDecimal.valueOf(30000.0));
        transaction1.setTotalAmount(BigDecimal.valueOf(60000.0));
        transaction1.setDateTransaction(LocalDateTime.now());

        transaction2 = new Transaction();
        transaction2.setId(101L);
        transaction2.setUser(user);
        transaction2.setCryptoName("ETH");
        transaction2.setType("SELL");
        transaction2.setQuantity(BigDecimal.valueOf(1.5));
        transaction2.setUnitPrice(BigDecimal.valueOf(2000.0));
        transaction2.setTotalAmount(BigDecimal.valueOf(3000.0));
        transaction2.setDateTransaction(LocalDateTime.now());
    }

    @Test
    void toDto_shouldMapTransactionToDto() {
        // Arrange & Act
        TransactionDto dto = TransactionMapper.toDto(transaction1);

        // Assert
        assertThat(dto.getCryptoName()).isEqualTo("BTC");
        assertThat(dto.getType()).isEqualTo("BUY");
        assertThat(dto.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(2.0));
        assertThat(dto.getUnitPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000.0));
        assertThat(dto.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(60000.0));
        assertThat(dto.getDateTransaction()).isEqualTo(transaction1.getDateTransaction());
    }

    @Test
    void dtoList_shouldMapListOfTransactionsToListOfDtos() {
        // Arrange
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        // Act
        List<TransactionDto> dtos = TransactionMapper.dtoList(transactions);

        // Assert
        assertThat(dtos).hasSize(2);

        // Vérifier le premier élément
        TransactionDto dto1 = dtos.get(0);
        assertThat(dto1.getCryptoName()).isEqualTo("BTC");
        assertThat(dto1.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(2.0));

        // Vérifier le second élément
        TransactionDto dto2 = dtos.get(1);
        assertThat(dto2.getCryptoName()).isEqualTo("ETH");
        assertThat(dto2.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(1.5));
    }
}
