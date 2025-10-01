package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import com.nexora.nexora_crypto_api.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptoWalletService cryptoWalletService;

    @Mock
    private CryptoWalletRepository cryptoWalletRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

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
    void buyCrypto_shouldCreateTransactionAndUpdateWallet() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(BigDecimal.valueOf(2000));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(userId);
        transactionDto.setCryptoName("bitcoin");
        transactionDto.setQuantity(BigDecimal.valueOf(0.5));
        transactionDto.setUnitPrice(BigDecimal.valueOf(1000));
        transactionDto.setTotalAmount(BigDecimal.valueOf(500));

        CryptoWallet wallet = new CryptoWallet();
        wallet.setQuantity(BigDecimal.ZERO);

        when(userService.getUserById(userId)).thenReturn(user);
        when(cryptoWalletService.getOrCreateWallet("bitcoin", user)).thenReturn(wallet);

        // Act
        transactionService.buyCrypto(transactionDto);

        // Assert
        verify(userRepository).save(user);
        verify(cryptoWalletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));

        assertTrue(user.getBalance().compareTo(BigDecimal.valueOf(1500)) == 0);
        assertEquals(BigDecimal.valueOf(0.5), wallet.getQuantity());
    }

    @Test
    void buyCrypto_shouldThrowExceptionWhenBalanceTooLow() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(BigDecimal.valueOf(100));

        TransactionDto dto = new TransactionDto();
        dto.setUserId(userId);
        dto.setCryptoName("bitcoin");
        dto.setQuantity(BigDecimal.valueOf(1));
        dto.setUnitPrice(BigDecimal.valueOf(200));
        dto.setTotalAmount(BigDecimal.valueOf(200));

        when(userService.getUserById(userId)).thenReturn(user);

        assertThrows(RuntimeException.class, () -> transactionService.buyCrypto(dto));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void sellCrypto_shouldCreateTransactionAndUpdateBalance() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(BigDecimal.valueOf(1000));

        CryptoWallet wallet = new CryptoWallet();
        wallet.setQuantity(BigDecimal.valueOf(2));

        TransactionDto dto = new TransactionDto();
        dto.setUserId(userId);
        dto.setCryptoName("bitcoin");
        dto.setQuantity(BigDecimal.valueOf(1));
        dto.setUnitPrice(BigDecimal.valueOf(300));

        when(userService.getUserById(userId)).thenReturn(user);
        when(cryptoWalletService.getOrCreateWallet("bitcoin", user)).thenReturn(wallet);

        transactionService.sellCrypto(dto);

        verify(cryptoWalletRepository).save(wallet);
        verify(userRepository).save(user);
        verify(transactionRepository).save(any(Transaction.class));

        assertEquals(BigDecimal.valueOf(1300), user.getBalance());
        assertEquals(BigDecimal.valueOf(1), wallet.getQuantity());
    }

    @Test
    void sellCrypto_shouldThrowExceptionWhenNotEnoughCrypto() {
        Long userId = 1L;
        User user = new User();

        CryptoWallet wallet = new CryptoWallet();
        wallet.setQuantity(BigDecimal.ZERO);

        TransactionDto dto = new TransactionDto();
        dto.setUserId(userId);
        dto.setCryptoName("bitcoin");
        dto.setQuantity(BigDecimal.ONE);

        when(userService.getUserById(userId)).thenReturn(user);
        when(cryptoWalletService.getOrCreateWallet("bitcoin", user)).thenReturn(wallet);

        assertThrows(RuntimeException.class, () -> transactionService.sellCrypto(dto));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getAllTransactionsById_shouldReturnTransactions() {
        Long userId = 1L;
        List<Transaction> expected = Collections.singletonList(new Transaction());

        when(transactionRepository.findByUserId(userId)).thenReturn(expected);

        List<Transaction> result = transactionService.getAllTransactionsById(userId);
        assertEquals(1, result.size());
    }
}