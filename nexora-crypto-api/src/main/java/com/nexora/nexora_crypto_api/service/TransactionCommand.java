package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.dto.TransactionDto;

public interface TransactionCommand {
    void execute(TransactionDto request);
}
