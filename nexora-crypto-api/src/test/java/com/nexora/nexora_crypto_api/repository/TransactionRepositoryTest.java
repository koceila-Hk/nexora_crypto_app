package com.nexora.nexora_crypto_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    private User user;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("buyer@example.com");

        transaction1 = new Transaction();
        transaction1.setId(100L);
        transaction1.setUser(user);
        transaction1.setCryptoName("BTC");
        transaction1.setType("BUY");
        transaction1.setQuantity(BigDecimal.valueOf(2.0));
        transaction1.setTotalAmount(BigDecimal.valueOf(60000.0));

        transaction2 = new Transaction();
        transaction2.setId(101L);
        transaction2.setUser(user);
        transaction2.setCryptoName("BTC");
        transaction2.setType("BUY");
        transaction2.setQuantity(BigDecimal.valueOf(1.0));
        transaction2.setTotalAmount(BigDecimal.valueOf(31000.0));
    }

    @Test
    void findByUserId_shouldReturnTransactions() {
        when(transactionRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> result = transactionRepository.findByUserId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCryptoName()).isEqualTo("BTC");
        assertThat(result.get(1).getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(1.0));

    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoTransactions() {
        when(transactionRepository.findByUserId(2L)).thenReturn(Collections.emptyList());

        List<Transaction> result = transactionRepository.findByUserId(2L);
        assertThat(result).isEmpty();
    }

    @Test
    void findTotalAmountAndQuantityByUserAndCrypto_shouldReturnEmpty_whenNoData() {
        when(transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(2L, "ETH")).thenReturn(Collections.emptyList());

        List<Object[]> result = transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(2L, "ETH");
        assertThat(result).isEmpty();
    }
}
