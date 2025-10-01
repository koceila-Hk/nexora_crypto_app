package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private CryptoWalletService cryptoWalletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWalletsWithVariation_shouldReturnWallets() {
        // Arrange
        Long userId = 1L;

        WalletDetailDto wallet1 = new WalletDetailDto();
        wallet1.setCryptoName("BTC");
        wallet1.setQuantity(BigDecimal.valueOf(2.5));
        wallet1.setVariationPercentage(BigDecimal.valueOf(10.0));

        WalletDetailDto wallet2 = new WalletDetailDto();
        wallet2.setCryptoName("ETH");
        wallet2.setQuantity(BigDecimal.valueOf(5.0));
        wallet2.setVariationPercentage(BigDecimal.valueOf(-5.0));

        when(cryptoWalletService.getWalletsWithVariation(userId))
                .thenReturn(List.of(wallet1, wallet2));

        // Act
        ResponseEntity<List<WalletDetailDto>> response = walletController.getWalletsWithVariation(userId);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getCryptoName()).isEqualTo("BTC");
        assertThat(response.getBody().get(1).getVariationPercentage()).isEqualTo(BigDecimal.valueOf(-5.0));

        verify(cryptoWalletService, times(1)).getWalletsWithVariation(userId);
    }

    @Test
    void getWalletsWithVariation_shouldReturnEmptyList_whenNull() {
        // Arrange
        Long userId = 2L;
        when(cryptoWalletService.getWalletsWithVariation(userId)).thenReturn(null);

        // Act
        ResponseEntity<List<WalletDetailDto>> response = walletController.getWalletsWithVariation(userId);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();

        verify(cryptoWalletService, times(1)).getWalletsWithVariation(userId);
    }
}
