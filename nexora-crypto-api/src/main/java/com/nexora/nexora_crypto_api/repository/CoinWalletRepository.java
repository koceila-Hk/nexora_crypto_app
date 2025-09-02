package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.CoinWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoinWalletRepository extends JpaRepository <CoinWallet, Long> {
    Optional<CoinWallet> findByUserIdAndCryptoName(Long userId, String cryptoName);

    List<CoinWallet> findByUserId(Long userId);
}
