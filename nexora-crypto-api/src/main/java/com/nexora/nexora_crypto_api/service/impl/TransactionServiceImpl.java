package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.TransactionRequest;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import com.nexora.nexora_crypto_api.service.TransactionService;
import com.nexora.nexora_crypto_api.service.UserService;
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
        User user = userService.getUserById(request.getUserId());
        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        // Déduction de la balance
        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);

        // Création de la transaction
        Transaction transaction = new Transaction();
        transaction.setType("BUY");
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(request.getUnitPrice());
        transaction.setTotalAmount(totalAmount);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        // Mise à jour ou création du wallet
        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);
        wallet.setQuantity(wallet.getQuantity().add(request.getQuantity()));
        walletRepository.save(wallet);
    }
}
