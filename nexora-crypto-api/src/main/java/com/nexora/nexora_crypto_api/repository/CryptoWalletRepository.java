package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository <CryptoWallet, Long> {
}
