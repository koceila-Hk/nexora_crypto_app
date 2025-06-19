package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;

import java.util.List;

public interface CryptoWalletService {
    CryptoWallet getOrCreateWallet(String cryptoName, User user);
    List<CryptoWallet> getWalletsWithVariation(Long userId);
}
