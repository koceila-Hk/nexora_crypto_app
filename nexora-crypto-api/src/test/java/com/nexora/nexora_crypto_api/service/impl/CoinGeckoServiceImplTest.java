package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoCache;
import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.repository.CryptoCacheRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinGeckoServiceImplTest {

    @Mock
    private CryptoCacheRepository cryptoCacheRepository;

    @Spy
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
        // GIVEN un cache existant
        CryptoCache cache = new CryptoCache();
        cache.setId("bitcoin");
        cache.setName("Bitcoin");
        cache.setSymbol("btc");
        cache.setIcon("icon_url");
        cache.setPrice(new BigDecimal("30000.00"));
        cache.setPercentageChange(new BigDecimal("5.2"));
        cache.setCachedAt(new Date());

        when(cryptoCacheRepository.findById("bitcoin")).thenReturn(Optional.of(cache));

        // WHEN
        CoinInfosForUserDto result = coinGeckoService.getCoinDetails("bitcoin", "eur");

        // THEN
        assertNotNull(result);
        assertEquals("Bitcoin", result.getCryptoName());
        assertEquals("btc", result.getSymbol());
        assertEquals("icon_url", result.getIcon());
        assertEquals(new BigDecimal("30000.00"), result.getCurrentPrice());
        assertEquals(new BigDecimal("5.2"), result.getPriceChangePercentage());
        verify(cryptoCacheRepository, never()).save(any());
    }

    @Test
    void getCoinDetails_shouldFetchAndCache_whenCacheEmpty() {
        when(cryptoCacheRepository.findById("ethereum")).thenReturn(Optional.empty());

        // Mock du résultat de fetchAndCacheCoinGecko pour éviter appel réel
        CoinInfosForUserDto mockedCoin = new CoinInfosForUserDto(
                "Ethereum", "eth", "icon_url", new BigDecimal("3000.00"), new BigDecimal("5.5")
        );
        doReturn(mockedCoin).when(coinGeckoService).fetchAndCacheCoinGecko("ethereum", "eur");

        CoinInfosForUserDto result = coinGeckoService.getCoinDetails("ethereum", "eur");

        assertNotNull(result);
        assertEquals("Ethereum", result.getCryptoName());
        assertEquals("eth", result.getSymbol());

        // Vérifie que save n'est jamais appelé puisque fetch est mocké
        verify(cryptoCacheRepository, never()).save(any());
    }

}
