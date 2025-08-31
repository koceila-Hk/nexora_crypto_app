package com.nexora.nexora_crypto_api.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "coin_details")
public class CoinCache {
    @Id
    private String id;

    private String name;
    private String symbol;
    private String icon;
    private BigDecimal price;
    private BigDecimal percentageChange;

    @Indexed(expireAfterSeconds = 240)
    private Date cachedAt;
}
