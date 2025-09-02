package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.model.CoinWallet;
import com.nexora.nexora_crypto_api.model.User;

import java.util.List;

public interface CoinWalletService {
    CoinWallet getOrCreateWallet(String cryptoName, User user);
    List<WalletDetailDto> getWalletsWithVariation(Long userId);
}
