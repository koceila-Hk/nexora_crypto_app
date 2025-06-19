package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
