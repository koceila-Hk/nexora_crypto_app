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
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SellCryptoCommand implements TransactionCommand {
    private final TransactionRequest request;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CryptoWalletService cryptoWalletService;
    private final CryptoWalletRepository walletRepository;

    public SellCryptoCommand(
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
        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);

        if (wallet.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new RuntimeException("Crypto insuffisante dans le wallet");
        }

        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        // Déduire les cryptos du wallet
        wallet.setQuantity(wallet.getQuantity().subtract(request.getQuantity()));
        walletRepository.save(wallet);

        // Ajouter le montant au solde utilisateur
        user.setBalance(user.getBalance().add(totalAmount));
        userRepository.save(user);

        // Créer la transaction
        Transaction transaction = new Transaction();
        transaction.setType("SELL");
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(request.getUnitPrice());
        transaction.setTotalAmount(totalAmount);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }
}