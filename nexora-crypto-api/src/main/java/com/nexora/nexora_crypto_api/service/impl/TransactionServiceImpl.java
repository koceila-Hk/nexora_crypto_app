package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CryptoWalletRepository walletRepository;
    @Autowired
    private CryptoWalletService cryptoWalletService;

    public void buyCrypto(TransactionRequest request) {
        TransactionCommand buyCommand = new BuyCryptoCommand(
                request, userService, userRepository,
                transactionRepository, cryptoWalletService,
                walletRepository
        );

        TransactionInvoker invoker = new TransactionInvoker();
        invoker.setCommand(buyCommand);
        invoker.process();
    }

    public void sellCrypto(TransactionRequest request) {
        TransactionCommand sellCommand = new SellCryptoCommand(
                request, userService, userRepository,
                transactionRepository, cryptoWalletService,
                walletRepository
        );

        TransactionInvoker invoker = new TransactionInvoker();
        invoker.setCommand(sellCommand);
        invoker.process();
    }

}