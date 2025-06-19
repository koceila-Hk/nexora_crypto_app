package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<String> buyCrypto(@RequestBody TransactionRequest request) {
        try {
            transactionService.buyCrypto(request);
            return ResponseEntity.ok("Successful purchase !");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCrypto(@RequestBody TransactionRequest request) {
        try {
            transactionService.sellCrypto(request);
            return ResponseEntity.ok("Successful sale !");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
