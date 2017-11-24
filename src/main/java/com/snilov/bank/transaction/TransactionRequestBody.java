package com.snilov.bank.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionRequestBody {

    @NotNull(message = "UUID card cannot be empty")
    private String uuidCard;

    @NotNull(message = "Transaction type cannot by empty")
    private Transaction.TypeTransaction typeTransaction;

    @NotNull(message = "Transaction amount cannot be empty")
    private Integer transactionAmount;
}
