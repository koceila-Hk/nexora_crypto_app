package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionInvoker invoker;
    @Autowired
    private BuyCryptoCommand buyCryptoCommand;
    @Autowired
    private SellCryptoCommand sellCryptoCommand;
    @Autowired
    private TransactionRepository transactionRepository;

    public void buyCrypto(TransactionDto request) {
        invoker.process(buyCryptoCommand, request);
    }

    public void sellCrypto(TransactionDto request) {
        invoker.process(sellCryptoCommand, request);
    }

    @Override
    public List<Transaction> getAllTransactionsById(Long userId) {
        return transactionRepository.findByUserId((userId));
    }

}