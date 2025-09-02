package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CoinCache;
import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.repository.CoinCacheRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoinGeckoServiceImplTest {

    @Mock
    private CoinCacheRepository coinCacheRepository;

    @InjectMocks
    private CoinGeckoServiceImpl coinGeckoService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getCoinDetails_shouldReturnFromCache() {
        CoinCache cache = new CoinCache();
        cache.setId("bitcoin");
        cache.setName("Bitcoin");
        cache.setSymbol("btc");
        cache.setIcon("icon_url");
        cache.setPrice(new BigDecimal("30000.00"));
        cache.setPercentageChange(new BigDecimal("5.2"));
        cache.setCachedAt(new Date());

        when(coinCacheRepository.findById("bitcoin")).thenReturn(Optional.of(cache));

        CoinInfosForUserDto result = coinGeckoService.getCoinDetails("bitcoin", "eur");

        assertNotNull(result);
        assertEquals("Bitcoin", result.getCryptoName());
        assertEquals("btc", result.getSymbol());
        assertEquals("icon_url", result.getIcon());
        assertEquals(new BigDecimal("30000.00"), result.getCurrentPrice());
        assertEquals(new BigDecimal("5.2"), result.getPriceChangePercentage());
    }
}
