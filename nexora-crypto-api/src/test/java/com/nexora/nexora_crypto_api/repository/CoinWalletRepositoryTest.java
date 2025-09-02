package com.nexora.nexora_crypto_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.nexora.nexora_crypto_api.model.CoinWallet;
import com.nexora.nexora_crypto_api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoinWalletRepositoryTest {

    @Mock
    private CoinWalletRepository coinWalletRepository;

    private User user;
    private CoinWallet wallet1;
    private CoinWallet wallet2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        wallet1 = CoinWallet.builder()
                .id(100L)
                .user(user)
                .cryptoName("BTC")
                .quantity(BigDecimal.valueOf(2.5))
                .variationPercentage(BigDecimal.valueOf(10.0))
                .build();

        wallet2 = CoinWallet.builder()
                .id(101L)
                .user(user)
                .cryptoName("ETH")
                .quantity(BigDecimal.valueOf(5.0))
                .variationPercentage(BigDecimal.valueOf(5.0))
                .build();
    }

    @Test
    void findByUserIdAndCryptoName_shouldReturnWallet() {
        // Arrange
        when(coinWalletRepository.findByUserIdAndCryptoName(1L, "BTC")).thenReturn(Optional.of(wallet1));

        // Act
        Optional<CoinWallet> result = coinWalletRepository.findByUserIdAndCryptoName(1L, "BTC");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCryptoName()).isEqualTo("BTC");
        assertThat(result.get().getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
        assertThat(result.get().getVariationPercentage()).isEqualByComparingTo(BigDecimal.valueOf(10.0));
    }

    @Test
    void findByUserIdAndCryptoName_shouldReturnEmpty_whenNotFound() {
        when(coinWalletRepository.findByUserIdAndCryptoName(1L, "DOGE")).thenReturn(Optional.empty());

        Optional<CoinWallet> result = coinWalletRepository.findByUserIdAndCryptoName(1L, "DOGE");

        assertThat(result).isEmpty();
    }

    @Test
    void findByUserId_shouldReturnWallets() {
        when(coinWalletRepository.findByUserId(1L)).thenReturn(Arrays.asList(wallet1, wallet2));

        List<CoinWallet> result = coinWalletRepository.findByUserId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCryptoName()).isEqualTo("BTC");
        assertThat(result.get(0).getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
        assertThat(result.get(1).getCryptoName()).isEqualTo("ETH");
        assertThat(result.get(1).getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(5.0));
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoWallets() {
        when(coinWalletRepository.findByUserId(2L)).thenReturn(Collections.emptyList());

        List<CoinWallet> result = coinWalletRepository.findByUserId(2L);

        assertThat(result).isEmpty();
    }
}
