package com.nexora.nexora_crypto_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    private Long userId;
    private String cryptoName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
}