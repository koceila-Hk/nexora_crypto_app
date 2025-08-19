//package com.nexora.nexora_crypto_api.service.impl;
//
//import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
//import com.nexora.nexora_crypto_api.model.CryptoWallet;
//import com.nexora.nexora_crypto_api.model.Transaction;
//import com.nexora.nexora_crypto_api.model.User;
//import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
//import com.nexora.nexora_crypto_api.repository.TransactionRepository;
//import com.nexora.nexora_crypto_api.repository.UserRepository;
//import com.nexora.nexora_crypto_api.service.CryptoWalletService;
//import com.nexora.nexora_crypto_api.service.TransactionCommand;
//import com.nexora.nexora_crypto_api.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Service
//public class SellCryptoCommand implements TransactionCommand {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private CryptoWalletService cryptoWalletService;
//    @Autowired
//    private CryptoWalletRepository walletRepository;
//
//
//    @Override
//    public void execute(TransactionDto request) {
//        User user = userService.getUserById(request.getUserId());
//        CryptoWallet wallet = cryptoWalletService.getOrCreateWallet(request.getCryptoName(), user);
//
//        if (wallet.getQuantity().compareTo(request.getQuantity()) < 0) {
//            throw new RuntimeException("Crypto insuffisante dans le wallet");
//        }
//
//        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());
//
//        // Déduire les cryptos du wallet
//        wallet.setQuantity(wallet.getQuantity().subtract(request.getQuantity()));
//        walletRepository.save(wallet);
//
//        // Add amount for user
//        user.setBalance(user.getBalance().add(totalAmount));
//        userRepository.save(user);
//
//        // create transaction
//        Transaction transaction = new Transaction();
//        transaction.setType("SELL");
//        transaction.setCryptoName(request.getCryptoName());
//        transaction.setQuantity(request.getQuantity());
//        transaction.setUnitPrice(request.getUnitPrice());
//        transaction.setTotalAmount(totalAmount);
//        transaction.setDateTransaction(LocalDateTime.now());
//        transaction.setUser(user);
//        transactionRepository.save(transaction);
//    }
//}