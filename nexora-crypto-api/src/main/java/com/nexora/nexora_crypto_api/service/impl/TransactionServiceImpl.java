package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CoinWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.Transaction;
import com.nexora.nexora_crypto_api.repository.CoinWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoinWalletService coinWalletService;
    @Autowired
    private CoinWalletRepository walletRepository;


    /**
     * Effectue l'achat d'une cryptomonnaie pour un utilisateur donné.
     * Vérifie le solde, met à jour le portefeuille, crée la transaction.
     *
     * @param request DTO contenant les informations de l'achat
     * @throws RuntimeException si le solde est insuffisant
     */
    public void buyCrypto(TransactionDto request) {

        User user = userService.getUserById(request.getUserId());
        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }
        request.setTotalAmount(totalAmount);

        // deduct balance
        user.setBalance(user.getBalance().subtract(request.getTotalAmount()));
        userRepository.save(user);

        // create transaction
        createTransaction("BUY", request, user);

        CoinWallet wallet = coinWalletService.getOrCreateWallet(request.getCryptoName(), user);
        wallet.setQuantity(wallet.getQuantity().add(request.getQuantity()));
        walletRepository.save(wallet);
    }

    /**
     * Effectue la vente d'une cryptomonnaie pour un utilisateur.
     * Vérifie le solde du portefeuille, ajoute l'argent à l'utilisateur et enregistre la transaction.
     *
     * @param request DTO contenant les informations de la vente
     * @throws RuntimeException si le portefeuille ne contient pas assez de cryptomonnaie
     */
    public void sellCrypto(TransactionDto request) {

        User user = userService.getUserById(request.getUserId());
        CoinWallet wallet = coinWalletService.getOrCreateWallet(request.getCryptoName(), user);

        if (wallet.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new RuntimeException("Crypto insuffisante dans le wallet");
        }

        BigDecimal totalAmount = request.getQuantity().multiply(request.getUnitPrice());
        request.setTotalAmount(totalAmount);

        // Déduire les cryptos du wallet
        wallet.setQuantity(wallet.getQuantity().subtract(request.getQuantity()));
        walletRepository.save(wallet);

        // Add amount for user
        user.setBalance(user.getBalance().add(totalAmount));
        userRepository.save(user);

        // create transaction
        createTransaction("SELL", request, user);
    }


    /**
     * Récupère toutes les transactions d'un utilisateur donné.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des transactions de l'utilisateur
     */
    @Override
    public List<Transaction> getAllTransactionsById(Long userId) {
        return transactionRepository.findByUserId((userId));
    }

    /**
     * Crée et sauvegarde une transaction dans la base.
     *
     * @param type   Type de transaction ("BUY" ou "SELL")
     * @param request Données issues de l'opération de transaction
     * @param user   Utilisateur lié à la transaction
     */
    private void createTransaction(String type, TransactionDto request, User user) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setCryptoName(request.getCryptoName());
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(request.getUnitPrice());
        transaction.setTotalAmount(request.getTotalAmount());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }

}

