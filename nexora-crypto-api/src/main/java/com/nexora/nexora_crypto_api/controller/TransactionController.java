package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.dto.TransactionDto;
import com.nexora.nexora_crypto_api.mapper.TransactionMapper;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<String> buyCrypto(@RequestBody TransactionDto request) {
        try {
            transactionService.buyCrypto(request);
            return ResponseEntity.ok("Successful purchase !");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCrypto(@RequestBody TransactionDto request) {
        try {
            transactionService.sellCrypto(request);
            return ResponseEntity.ok("Successful sale !");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionDto>> getAllTransacations(@PathVariable long userId) {
        List<Transaction> transactionList = transactionService.getAllTransactionsById(userId);
        List<TransactionDto> dtoList = TransactionMapper.dtoList(transactionList);
        return ResponseEntity.ok(dtoList);
    }
}
