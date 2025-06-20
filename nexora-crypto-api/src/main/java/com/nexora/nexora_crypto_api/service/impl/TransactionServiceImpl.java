package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionInvoker invoker;
    @Autowired
    private BuyCryptoCommand buyCryptoCommand;
    @Autowired
    private SellCryptoCommand sellCryptoCommand;

    public void buyCrypto(TransactionRequest request) {
        invoker.process(buyCryptoCommand, request);
    }

    public void sellCrypto(TransactionRequest request) {
        invoker.process(sellCryptoCommand, request);
    }

}