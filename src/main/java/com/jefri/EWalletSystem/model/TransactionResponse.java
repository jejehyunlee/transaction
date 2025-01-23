package com.jefri.EWalletSystem.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.math.BigDecimal;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private String message;

    private Long trxId;

    private BigDecimal new_balance;

    private BigDecimal currentBalance;

}
