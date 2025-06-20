package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;

public interface TransactionCommand {
    void execute(TransactionRequest request);
}
