package com.snilov.bank.utils;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.model.enums.TypeTransactionEnum;
import com.snilov.bank.requestBody.TransactionRequestBody;
import com.snilov.bank.service.TransactionService;


public class DWTransactionDataBuilder {

    private final TransactionService transactionService;

    private TransactionRequestBody transactionRequestBody;

    public DWTransactionDataBuilder(TransactionService transactionService, TypeTransactionEnum type) {
        this.transactionService = transactionService;
        transactionRequestBody = TransactionRequestBody.builder().typeTransaction(type).build();
    }

    public DWTransactionDataBuilder uuidCard(String uuid) {
        transactionRequestBody.setUuidCard(uuid);
        return this;
    }

    public DWTransactionDataBuilder amount(Integer amount) {
        transactionRequestBody.setTransactionAmount(amount);
        return this;
    }

    public Transaction save() {
        return transactionService.createNewTransaction(transactionRequestBody);
    }
}
