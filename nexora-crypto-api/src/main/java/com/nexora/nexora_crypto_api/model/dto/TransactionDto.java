package com.nexora.nexora_crypto_api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {
    private Long userId;
    private String cryptoName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String type;

    private BigDecimal totalAmount;
    private LocalDateTime dateTransaction;
}