package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;

public interface CryptoWalletService {
    CryptoWallet getOrCreateWallet(String cryptoName, User user);
}
