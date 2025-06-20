package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoWalletRepository extends JpaRepository <CryptoWallet, Long> {
    Optional<CryptoWallet> findByUserAndCryptoName(User user, String cryptoName);

}
