package com.nexora.nexora_crypto_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinInfosForUserDto {
    private String cryptoName;
    private String symbol;
    private String icon;
    private BigDecimal currentPrice;
    private BigDecimal priceChangePercentage;

}
