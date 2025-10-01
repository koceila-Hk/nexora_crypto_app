package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId")
    List<Transaction> findByUserId(Long userId);

    @Query("SELECT SUM(t.totalAmount), SUM(t.quantity) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.cryptoName = :cryptoName AND t.type = 'BUY'")
    List<Object[]> findTotalAmountAndQuantityByUserAndCrypto(@Param("userId") Long userId, @Param("cryptoName") String cryptoName);
}
