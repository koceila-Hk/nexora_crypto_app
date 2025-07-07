package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.mappers.TransactionMapper;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.response.ApiResponse;
import com.nexora.nexora_crypto_api.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buyCrypto(@Valid @RequestBody TransactionDto request) {
        try {
            transactionService.buyCrypto(request);
            logger.info("Buy crypto successful: {}", request.getCryptoName());
            return ResponseEntity.ok(new ApiResponse("Successful purchase !"));
        } catch (RuntimeException e) {
            logger.warn("Buy crypto failed: {}, errorMessage: {}", request.getCryptoName(),e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<ApiResponse> sellCrypto(@Valid @RequestBody TransactionDto request) {
        try {
            transactionService.sellCrypto(request);
            logger.info("Sell crypto successful: {}", request.getCryptoName());
            return ResponseEntity.ok(new ApiResponse("Successful sale !"));
        } catch (RuntimeException e) {
            logger.warn("Sell crypto failed: {}, errorMessage: {}", request.getCryptoName(),e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionDto>> getAllTransacations(@PathVariable long userId) {
        List<Transaction> transactionList = transactionService.getAllTransactionsById(userId);
        List<TransactionDto> dtoList = TransactionMapper.dtoList(transactionList);
        return ResponseEntity.ok(dtoList);
    }
}
