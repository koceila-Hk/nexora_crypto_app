package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.model.Transaction;

public interface TransactionService {

    void buyCrypto(TransactionRequest request);
}
