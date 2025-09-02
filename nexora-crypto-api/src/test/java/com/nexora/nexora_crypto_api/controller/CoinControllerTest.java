package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CoinControllerTest {

    @InjectMocks
    private CoinController coinController;

    @Mock
    private CoinGeckoService coinGeckoService;

    private CoinInfosForUserDto coinDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        coinDto = new CoinInfosForUserDto();
        coinDto.setCryptoName("Bitcoin");
        coinDto.setSymbol("BTC");
        coinDto.setIcon("bitcoin-icon.png");
        coinDto.setCurrentPrice(BigDecimal.valueOf(30000.0));
        coinDto.setPriceChangePercentage(BigDecimal.valueOf(5.0));
    }

    @Test
    void getCoinDetails_shouldReturnCoinInfo() {
        // Arrange
        String coinId = "bitcoin";
        String currency = "eur";
        when(coinGeckoService.getCoinDetails(coinId, currency)).thenReturn(coinDto);

        // Act
        ResponseEntity<CoinInfosForUserDto> response = coinController.getCoinDetails(coinId, currency);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCryptoName()).isEqualTo("Bitcoin");
        assertThat(response.getBody().getCurrentPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000.0));
        assertThat(response.getBody().getPriceChangePercentage()).isEqualByComparingTo(BigDecimal.valueOf(5.0));

        verify(coinGeckoService, times(1)).getCoinDetails(coinId, currency);
    }

    @Test
    void getCoinDetails_shouldReturnDefaultCurrencyWhenNotProvided() {
        // Arrange
        String coinId = "bitcoin";
        when(coinGeckoService.getCoinDetails(coinId, "eur")).thenReturn(coinDto);

        // Act
        ResponseEntity<CoinInfosForUserDto> response = coinController.getCoinDetails(coinId, "eur");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCryptoName()).isEqualTo("Bitcoin");

        verify(coinGeckoService, times(1)).getCoinDetails(coinId, "eur");
    }

    @Test
    void getCoinDetails_shouldHandleServiceException() {
        // Arrange
        String coinId = "bitcoin";
        String currency = "eur";
        when(coinGeckoService.getCoinDetails(coinId, currency))
                .thenThrow(new RuntimeException("Coin not found"));

        // Act & Assert
        try {
            coinController.getCoinDetails(coinId, currency);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Coin not found");
        }

        verify(coinGeckoService, times(1)).getCoinDetails(coinId, currency);
    }
}
