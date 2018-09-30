package com.snilov.bank.requestBody;

import com.snilov.bank.model.enums.TypeTransactionEnum;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TransactionRequestBody {

    @NotNull(message = "UUID card cannot be empty")
    private String uuidCard;

    @NotNull(message = "Transaction type cannot by empty")
    private TypeTransactionEnum typeTransaction;

    @NotNull(message = "Transaction amount cannot be empty")
    private Integer transactionAmount;

    @AssertTrue(message = "Type transaction 'DEPOSIT' must be a positive pan")
    private boolean isDepositOk() {
        if (transactionAmount == null || uuidCard == null || typeTransaction == null) return true;
        return getTypeTransaction() != TypeTransactionEnum.DEPOSIT || getTransactionAmount() >= 0;
    }

    @AssertTrue(message = "Type transaction 'WITHDRAW' must be a negative pan")
    private boolean isWithdrawOk() {
        if (transactionAmount == null || uuidCard == null || typeTransaction == null) return true;
        return getTypeTransaction() != TypeTransactionEnum.WITHDRAW || getTransactionAmount() <= 0;
    }
}
