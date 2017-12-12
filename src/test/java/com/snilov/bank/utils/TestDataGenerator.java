package com.snilov.bank.utils;

import com.snilov.bank.model.enums.TypeTransactionEnum;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import com.snilov.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDataGenerator {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionService transactionService;

    public AccountDataBuilder account() {
        return new AccountDataBuilder(accountRepository);
    }

    public CardDataBuilder card() {
        return new CardDataBuilder(cardRepository);
    }

    public DWTransactionDataBuilder transactionDeposit() {
        return new DWTransactionDataBuilder(transactionService, TypeTransactionEnum.DEPOSIT);
    }

    public RollbackTransactionDataBuilder transactionRollback() {
        return new RollbackTransactionDataBuilder(transactionService);
    }

    public TransferTransactionDataBuilder transactionTransfer() {
        return new TransferTransactionDataBuilder(transactionService);
    }
}
