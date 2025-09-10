package com.nexora.nexora_crypto_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    private String type;

    @Column(name = "crypto_name")
    private String cryptoName;

    @Column(precision = 15, scale = 5)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 15, scale = 5)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 15, scale = 5)
    private BigDecimal totalAmount;

    @Column(name = "date_transaction")
    private LocalDateTime dateTransaction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
