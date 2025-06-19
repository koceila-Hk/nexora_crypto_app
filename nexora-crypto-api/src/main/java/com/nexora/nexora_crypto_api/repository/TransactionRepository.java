package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    @Query("SELECT SUM(t.totalAmount), SUM(t.quantity) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.cryptoName = :cryptoName AND t.type = 'BUY'")
    Object[] findTotalAmountAndQuantityByUserAndCrypto(@Param("userId") Long userId, @Param("cryptoName") String cryptoName);

}
