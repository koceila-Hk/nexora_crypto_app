package com.nexora.nexora_backend.repository;

import com.nexora.nexora_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
