package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
//    @Autowired
//    private TransactionInvoker invoker;
//    @Autowired
//    private BuyCryptoCommand buyCryptoCommand;
//    @Autowired
//    private SellCryptoCommand sellCryptoCommand;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoWalletService cryptoWalletService;
    @Autowired
    private CryptoWalletRepository walletRepository;

    public void buyCrypto(TransactionDto request) {
//        invoker.process(buyCryptoCommand, request);

        User user = userService.getUserById(request.getUserId());
        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        // deduct balance
        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);

        // Add transaction
        Transaction transaction = new Transaction();
        transaction.setType("BUY");
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity().setScale(5,RoundingMode.HALF_UP));
        transaction.setUnitPrice(request.getUnitPrice().setScale(5, RoundingMode.HALF_UP));
        transaction.setTotalAmount(totalAmount.setScale(5,RoundingMode.HALF_UP));
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);
        wallet.setQuantity(wallet.getQuantity().add(request.getQuantity()));
        walletRepository.save(wallet);
    }

    public void sellCrypto(TransactionDto request) {
//        invoker.process(sellCryptoCommand, request);

        User user = userService.getUserById(request.getUserId());
        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);

        if (wallet.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new RuntimeException("Crypto insuffisante dans le wallet");
        }

        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        // Déduire les cryptos du wallet
        wallet.setQuantity(wallet.getQuantity().subtract(request.getQuantity()));
        walletRepository.save(wallet);

        // Add amount for user
        user.setBalance(user.getBalance().add(totalAmount));
        userRepository.save(user);

        // create transaction
        Transaction transaction = new Transaction();
        transaction.setType("SELL");
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity().setScale(5,RoundingMode.HALF_UP));
        transaction.setUnitPrice(request.getUnitPrice().setScale(5, RoundingMode.HALF_UP));
        transaction.setTotalAmount(totalAmount.setScale(5,RoundingMode.HALF_UP));
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionsById(Long userId) {
        return transactionRepository.findByUserId((userId));
    }

}

