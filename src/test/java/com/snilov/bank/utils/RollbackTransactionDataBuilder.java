package com.snilov.bank.utils;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.service.TransactionService;

public class RollbackTransactionDataBuilder {

    private final TransactionService transactionService;

    private String uuidTransaction;

    public RollbackTransactionDataBuilder(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public RollbackTransactionDataBuilder uuidTransaction(String uuid) {
        uuidTransaction = uuid;
        return this;
    }

    public Transaction save() {
        return transactionService.rollbackTransaction(uuidTransaction);
    }
}
