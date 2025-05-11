package com.nexora.nexora_backend.repository;

import com.nexora.nexora_backend.model.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository <CryptoWallet, Long> {
}
