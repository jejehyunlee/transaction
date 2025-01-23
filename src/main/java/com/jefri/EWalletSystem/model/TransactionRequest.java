package com.jefri.EWalletSystem.model;


import com.jefri.EWalletSystem.entity.Transaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class TransactionRequest {

    private Long userId;

    private Long trxId;

    private String username;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Transaction.TransactionType type;




}
