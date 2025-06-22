package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import com.nexora.nexora_crypto_api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceImplTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CryptoWalletRepository walletRepository;

    @MockBean
    private CryptoWalletService cryptoWalletService;

    @Test
    void buyCrypto_shouldSucceed_whenUserHasEnoughBalance() {
        Long userId = 1L;
        String cryptoName = "Bitcoin";
        BigDecimal quantity = new BigDecimal("0.5");
        BigDecimal unitPrice = new BigDecimal("10000");
        BigDecimal totalAmount = quantity.multiply(unitPrice);
        BigDecimal initialBalance = new BigDecimal("15000");

        User user = new User();
        user.setId(userId);
        user.setBalance(initialBalance);

        CryptoWallet wallet = new CryptoWallet();
        wallet.setCryptoName(cryptoName);
        wallet.setQuantity(BigDecimal.ZERO);
        wallet.setUser(user);

        TransactionDto request = new TransactionDto();
        request.setUserId(userId);
        request.setCryptoName(cryptoName);
        request.setQuantity(quantity);
        request.setUnitPrice(unitPrice);

        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(cryptoWalletService.getOrCreateWallet(cryptoName, user)).thenReturn(wallet);

        transactionService.buyCrypto(request);

        // Assert
        BigDecimal expectedBalance = initialBalance.subtract(totalAmount);
        Assertions.assertEquals(expectedBalance, user.getBalance());
        Assertions.assertEquals(quantity, wallet.getQuantity());

        Mockito.verify(userRepository).save(user);
        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
        Mockito.verify(walletRepository).save(wallet);
    }

    @Test
    void buyCrypto_shouldThrowException_whenBalanceIsInsufficient() {
        Long userId = 1L;
        String cryptoName = "Bitcoin";
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal unitPrice = new BigDecimal("10000");
        BigDecimal totalAmount = quantity.multiply(unitPrice);
        BigDecimal balance = new BigDecimal("5000");

        User user = new User();
        user.setId(userId);
        user.setBalance(balance);

        TransactionDto request = new TransactionDto();
        request.setUserId(userId);
        request.setCryptoName(cryptoName);
        request.setQuantity(quantity);
        request.setUnitPrice(unitPrice);

        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            transactionService.buyCrypto(request);
        });

        Assertions.assertEquals("Solde insuffisant", exception.getMessage());

        // On vérifie que rien n’a été sauvegardé
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(walletRepository, Mockito.never()).save(Mockito.any());
    }

}