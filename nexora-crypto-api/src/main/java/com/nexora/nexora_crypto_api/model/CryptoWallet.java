package com.nexora.nexora_crypto_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "crypto_wallet")
public class CryptoWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @Column(name = "crypto_name")
    private String cryptoName;

    @Column(precision = 15, scale = 5)
    private BigDecimal quantity;

    @Transient
    private BigDecimal variationPercentage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
