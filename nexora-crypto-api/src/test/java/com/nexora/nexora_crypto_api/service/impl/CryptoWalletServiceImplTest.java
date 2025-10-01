package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoWalletServiceImplTest {

    @Mock
    private CryptoWalletRepository cryptoWalletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CoinGeckoService coinGeckoService;

    @InjectMocks
    private CryptoWalletServiceImpl coinWalletService;

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
    void getOrCreateWallet_shouldReturnExistingWallet() {
        User user = new User();
        user.setId(1L);

        CryptoWallet wallet = new CryptoWallet();
        when(cryptoWalletRepository.findByUserIdAndCryptoName(1L, "bitcoin")).thenReturn(Optional.of(wallet));

        CryptoWallet result = coinWalletService.getOrCreateWallet("bitcoin", user);
        assertEquals(wallet, result);
    }

    @Test
    void getOrCreateWallet_shouldCreateNewWallet() {
        User user = new User();
        user.setId(1L);

        when(cryptoWalletRepository.findByUserIdAndCryptoName(1L, "bitcoin")).thenReturn(Optional.empty());

        CryptoWallet savedWallet = new CryptoWallet();
        savedWallet.setCryptoName("bitcoin");
        savedWallet.setQuantity(BigDecimal.ZERO);
        savedWallet.setUser(user);

        when(cryptoWalletRepository.save(any())).thenReturn(savedWallet);

        CryptoWallet result = coinWalletService.getOrCreateWallet("bitcoin", user);

        assertEquals("bitcoin", result.getCryptoName());
        assertEquals(BigDecimal.ZERO, result.getQuantity());
    }

    @Test
    void getWalletsWithVariation_shouldReturnWalletsWithCorrectVariation() {

        User user = new User();
        user.setId(1L);

        // Mock portefeuille
        CryptoWallet wallet = new CryptoWallet();
        wallet.setCryptoName("ethereum");
        wallet.setQuantity(new BigDecimal("2.0"));

        when(cryptoWalletRepository.findByUserId(user.getId()))
                .thenReturn(List.of(wallet));

        // Mock transactions pour calcul de prix moyen
        Object[] transactionData = new Object[]{new BigDecimal("4000"), new BigDecimal("2.0")};
        when(transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(user.getId(), "ethereum"))
                .thenReturn(Collections.singletonList(transactionData));


        // Mock CoinGeckoService
        CoinInfosForUserDto coinDetails = new CoinInfosForUserDto();
        coinDetails.setCurrentPrice(new BigDecimal("2500")); // prix actuel
        coinDetails.setIcon("eth_icon_url");

        when(coinGeckoService.getCoinDetails("ethereum", "eur"))
                .thenReturn(coinDetails);

        // Execute
        List<WalletDetailDto> result = coinWalletService.getWalletsWithVariation(user.getId());

        assertEquals(1, result.size());

        WalletDetailDto dto = result.get(0);
        assertEquals("ethereum", dto.getCryptoName());
        assertEquals(new BigDecimal("2.0"), dto.getQuantity());
        assertEquals("eth_icon_url", dto.getIcon());

        // Variation = (2500 - 2000) / 2000 * 100 = 25%
        assertEquals(new BigDecimal("25.00000"), dto.getVariationPercentage());
    }

    @Test
    void getWalletsWithVariation_shouldSkipWalletWithNoTransactions() {
        User user = new User();
        user.setId(1L);

        CryptoWallet wallet = new CryptoWallet();
        wallet.setCryptoName("litecoin");

        when(cryptoWalletRepository.findByUserId(user.getId()))
                .thenReturn(List.of(wallet));

        // Pas de transactions pour ce portefeuille
        when(transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(user.getId(), "litecoin"))
                .thenReturn(List.of());

        List<WalletDetailDto> result = coinWalletService.getWalletsWithVariation(user.getId());

        // Pas de DTO généré car pas de transaction
        assertTrue(result.isEmpty());
    }

}