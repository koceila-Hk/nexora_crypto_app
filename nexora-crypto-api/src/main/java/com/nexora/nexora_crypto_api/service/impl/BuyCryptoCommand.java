package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import com.nexora.nexora_crypto_api.service.TransactionCommand;
import com.nexora.nexora_crypto_api.service.UserService;
import org.h2.command.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BuyCryptoCommand implements TransactionCommand {


    private final TransactionRequest request;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CryptoWalletService cryptoWalletService;
    private final CryptoWalletRepository walletRepository;

    public BuyCryptoCommand(
            TransactionRequest request,
            UserService userService,
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            CryptoWalletService cryptoWalletService,
            CryptoWalletRepository walletRepository
    ) {
        this.request = request;
        this.userService = userService;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.cryptoWalletService = cryptoWalletService;
        this.walletRepository = walletRepository;
    }

    @Override
    public void execute() {
        User user = userService.getUserById(request.getUserId());
        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setType("BUY");
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(request.getUnitPrice());
        transaction.setTotalAmount(totalAmount);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);
        wallet.setQuantity(wallet.getQuantity().add(request.getQuantity()));
        walletRepository.save(wallet);
    }

}
