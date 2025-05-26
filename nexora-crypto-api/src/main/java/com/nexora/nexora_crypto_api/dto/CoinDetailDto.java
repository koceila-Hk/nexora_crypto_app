package com.nexora.nexora_crypto_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class CoinDetailDto {
    private String id;
    private String symbol;
    private String name;
    private MarketData market_data;
    private Image image;
    private Links links;

    @Data
    public static class MarketData {
        private Map<String, BigDecimal> current_price;
        private BigDecimal price_change_percentage_24h;
    }

    @Data
    public static class Image {
        private String thumb;
        private String small;
        private String large;
    }

    @Data
    public static class Links {
        private List<String> homepage;
    }
}

