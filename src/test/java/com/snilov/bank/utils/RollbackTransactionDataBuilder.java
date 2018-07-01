package com.snilov.bank.utils;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.service.TransactionService;

import java.util.List;

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

    public List<Transaction> save() {
        return transactionService.rollbackTransaction(uuidTransaction);
    }
}
