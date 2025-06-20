package com.nexora.nexora_crypto_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CoinInfosForUserDto {
    private String cryptoName;
    private String symbol;
    private String icon;
    private BigDecimal currentPrice;
    private BigDecimal priceChangePercentage;
}
