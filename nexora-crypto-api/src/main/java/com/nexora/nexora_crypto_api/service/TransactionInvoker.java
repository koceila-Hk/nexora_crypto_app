package com.nexora.nexora_crypto_api.service;


import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionInvoker {
    public void process(TransactionCommand command, TransactionDto request) {
        command.execute(request);
    }
}
