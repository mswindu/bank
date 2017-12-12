package com.snilov.bank.utils;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.model.enums.TypeTransferEnum;
import com.snilov.bank.requestBody.TransferRequestBody;
import com.snilov.bank.service.TransactionService;

import java.util.List;

public class TransferTransactionDataBuilder {

    private final TransactionService transactionService;

    private TransferRequestBody transferRequestBody;

    public TransferTransactionDataBuilder(TransactionService transactionService) {
        this.transactionService = transactionService;
        transferRequestBody = TransferRequestBody.builder().build();
    }

    public TransferTransactionDataBuilder type(TypeTransferEnum type) {
        transferRequestBody.setTypeTransferEnum(type);
        return this;
    }

    public TransferTransactionDataBuilder uuidPayer(String uuidPayer) {
        transferRequestBody.setUuidPayer(uuidPayer);
        return this;
    }

    public TransferTransactionDataBuilder uuidPayee(String uuidPayee) {
        transferRequestBody.setUuidPayee(uuidPayee);
        return this;
    }

    public TransferTransactionDataBuilder amount(Integer amount) {
        transferRequestBody.setAmount(amount);
        return this;
    }

    public List<Transaction> save() {
        return transactionService.transfer(transferRequestBody);
    }
}
