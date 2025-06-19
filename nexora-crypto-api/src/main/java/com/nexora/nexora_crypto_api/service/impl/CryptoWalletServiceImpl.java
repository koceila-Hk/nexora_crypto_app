package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;

    @Override
    public CryptoWallet getOrCreateWallet(String cryptoName, User user) {
        return cryptoWalletRepository.findByUserAndCryptoName(user, cryptoName).orElseGet(() -> {
            CryptoWallet wallet = new CryptoWallet();
            wallet.setUser(user);
            wallet.setCryptoName(cryptoName);
            wallet.setQuantity(BigDecimal.ZERO);
            return cryptoWalletRepository.save(wallet);
        });
    }
}
