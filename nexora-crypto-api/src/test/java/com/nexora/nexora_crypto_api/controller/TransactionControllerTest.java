package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.response.ApiResponse;
import com.nexora.nexora_crypto_api.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionDto = new TransactionDto();
        transactionDto.setCryptoName("BTC");
        transactionDto.setQuantity(BigDecimal.valueOf(1.5));
        transactionDto.setUnitPrice(BigDecimal.valueOf(60000));
        transactionDto.setTotalAmount(BigDecimal.valueOf(90000));
    }

    // ====== BUY CRYPTO ======
    @Test
    void buyCrypto_shouldReturnSuccess() {
        // Arrange
        doNothing().when(transactionService).buyCrypto(transactionDto);

        // Act
        ResponseEntity<ApiResponse> response = transactionController.buyCrypto(transactionDto);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getMessage()).isEqualTo("Successful purchase !");
        verify(transactionService, times(1)).buyCrypto(transactionDto);
    }

    @Test
    void buyCrypto_shouldReturnBadRequest_whenException() {
        // Arrange
        doThrow(new RuntimeException("Not enough balance")).when(transactionService).buyCrypto(transactionDto);

        // Act
        ResponseEntity<ApiResponse> response = transactionController.buyCrypto(transactionDto);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Not enough balance");
        verify(transactionService, times(1)).buyCrypto(transactionDto);
    }

    // ====== SELL CRYPTO ======
    @Test
    void sellCrypto_shouldReturnSuccess() {
        // Arrange
        doNothing().when(transactionService).sellCrypto(transactionDto);

        // Act
        ResponseEntity<ApiResponse> response = transactionController.sellCrypto(transactionDto);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getMessage()).isEqualTo("Successful sale !");
        verify(transactionService, times(1)).sellCrypto(transactionDto);
    }

    @Test
    void sellCrypto_shouldReturnBadRequest_whenException() {
        // Arrange
        doThrow(new RuntimeException("Not enough crypto")).when(transactionService).sellCrypto(transactionDto);

        // Act
        ResponseEntity<ApiResponse> response = transactionController.sellCrypto(transactionDto);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Not enough crypto");
        verify(transactionService, times(1)).sellCrypto(transactionDto);
    }

    // ====== GET ALL TRANSACTIONS ======
    @Test
    void getAllTransactions_shouldReturnList() {
        // Arrange
        long userId = 1L;
        Transaction transaction = new Transaction();
        transaction.setCryptoName("BTC");
        transaction.setQuantity(BigDecimal.valueOf(1.0));
        transaction.setUnitPrice(BigDecimal.valueOf(60000));
        transaction.setTotalAmount(BigDecimal.valueOf(60000));

        when(transactionService.getAllTransactionsById(userId)).thenReturn(List.of(transaction));

        // Act
        ResponseEntity<List<TransactionDto>> response = transactionController.getAllTransacations(userId);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getCryptoName()).isEqualTo("BTC");
        assertThat(response.getBody().get(0).getQuantity()).isEqualTo(BigDecimal.valueOf(1.0));
        verify(transactionService, times(1)).getAllTransactionsById(userId);
    }

    @Test
    void getAllTransactions_shouldReturnEmptyList_whenNoTransaction() {
        // Arrange
        long userId = 2L;
        when(transactionService.getAllTransactionsById(userId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<TransactionDto>> response = transactionController.getAllTransacations(userId);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
        verify(transactionService, times(1)).getAllTransactionsById(userId);
    }
}
