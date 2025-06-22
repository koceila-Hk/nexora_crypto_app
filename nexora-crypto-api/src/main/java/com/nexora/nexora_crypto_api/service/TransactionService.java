package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.Transaction;

import java.util.List;

public interface TransactionService {

    void buyCrypto(TransactionDto request);
    void sellCrypto(TransactionDto request);
    List<Transaction> getAllTransactionsById(Long userId);
}
