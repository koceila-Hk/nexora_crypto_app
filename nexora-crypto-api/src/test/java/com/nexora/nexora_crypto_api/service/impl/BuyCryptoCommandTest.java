package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class BuyCryptoCommandTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoWalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@gmail.com")
                .pseudonym("testuser")
                .password("secret")
                .balance(new BigDecimal("500"))
                .enabled(true)
                .build();
        user = userRepository.save(user);

        CryptoWallet wallet = CryptoWallet.builder()
                .cryptoName("BTC")
                .quantity(new BigDecimal("1"))
                .user(user)
                .build();
        walletRepository.save(wallet);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldBuyCrypto_whenBalanceIsSufficient() {
        TransactionDto request = new TransactionDto();
        request.setUserId(user.getId());
        request.setQuantity(new BigDecimal("2"));
        request.setUnitPrice(new BigDecimal("100"));
        request.setCryptoName("BTC");

        // exécution de la commande d'achat
        transactionService.buyCrypto(request);

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getBalance()).isEqualByComparingTo(new BigDecimal("300"));

        CryptoWallet updatedWallet = walletRepository.findByUserIdAndCryptoName(user.getId(), "BTC").orElseThrow();
        assertThat(updatedWallet.getQuantity()).isEqualByComparingTo(new BigDecimal("3"));
    }

    @Test
    void shouldThrowException_whenBalanceIsInsufficient() {
        User lowBalanceUser = User.builder()
                .email("lowbalance@gmail.com")
                .pseudonym("lowbalanceuser")
                .password("secret")
                .balance(new BigDecimal("50"))  // Solde insuffisant
                .enabled(true)
                .build();
        lowBalanceUser = userRepository.save(lowBalanceUser);

        TransactionDto request = new TransactionDto();
        request.setUserId(lowBalanceUser.getId());
        request.setQuantity(new BigDecimal("2"));
        request.setUnitPrice(new BigDecimal("100"));
        request.setCryptoName("BTC");

        // on attend une exception à l’exécution de la commande
        assertThrows(RuntimeException.class, () -> {
            transactionService.buyCrypto(request);
        });

        // solde n'a pas changé en base
        User userAfter = userRepository.findById(lowBalanceUser.getId()).orElseThrow();
        assertThat(userAfter.getBalance()).isEqualByComparingTo(new BigDecimal("50"));
    }

}