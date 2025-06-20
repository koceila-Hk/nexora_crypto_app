package com.nexora.nexora_crypto_api.service;


import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import org.springframework.stereotype.Component;

@Component
public class TransactionInvoker {
    public void process(TransactionCommand command, TransactionRequest request) {
        command.execute(request);
    }
}
