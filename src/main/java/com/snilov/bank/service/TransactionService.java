package com.snilov.bank.service;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.Transaction;
import com.snilov.bank.model.enums.TypeTransactionEnum;
import com.snilov.bank.model.enums.TypeTransferEnum;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.exception.CanNotCancelTransactionAgainException;
import com.snilov.bank.exception.CardIsBlockedException;
import com.snilov.bank.model.Card;
import com.snilov.bank.exception.ThereIsNoSuchTransactionException;
import com.snilov.bank.repository.TransactionRepository;
import com.snilov.bank.requestBody.TransactionRequestBody;
import com.snilov.bank.requestBody.TransferRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createNewTransaction(TransactionRequestBody transactionRequestBody) {
        Card card = cardService.getCard(transactionRequestBody.getUuidCard());

        if (card.getBlocked())
            throw new CardIsBlockedException("Card is blocked: " + transactionRequestBody.getUuidCard());

        Integer transactionAmount = transactionRequestBody.getTransactionAmount();

        Integer amountBefore = card.getAccount().getBalance();

        card.getAccount().setBalance(amountBefore + transactionAmount);
        accountRepository.save(card.getAccount());

        return transactionRepository.save(
                new Transaction(card.getAccount(), card, transactionRequestBody.getTypeTransaction(),
                        transactionAmount, new Date(), amountBefore, amountBefore + transactionAmount
                ));
    }

    public List<Transaction> rollbackTransaction(String uuidTransaction) {
        List<Transaction> transactions = new LinkedList<>();
        transactions.add(getTransaction(uuidTransaction));

        if (transactions.get(0).getIsCanceled())
            throw new CanNotCancelTransactionAgainException("You can not cancel the transaction again");

        if (transactions.get(0).getTypeTransaction() == TypeTransactionEnum.TRANSFER) {
            transactions.add(transactions.get(0).getLinkedTransaction());
        }

        for (int i = 0; i < transactions.size(); i++) {
            Transaction currentTransaction = transactions.get(i);
            Account account = currentTransaction.getAccount();
            Integer amountBefore = account.getBalance();
            account.setBalance(amountBefore - currentTransaction.getTransactionAmount());

            accountRepository.save(account);

            currentTransaction.setIsCanceled(true);
            transactionRepository.save(currentTransaction);
            transactions.set(i, currentTransaction);
        }

        return transactions;
    }

    public List<Transaction> transfer(TransferRequestBody transferRequestBody) {
        if (transferRequestBody.getTypeTransferEnum() == TypeTransferEnum.C2C) {
            return transferC2C(transferRequestBody);
        } else if (transferRequestBody.getTypeTransferEnum() == TypeTransferEnum.A2A) {
            return transferA2A(transferRequestBody);
        }

        throw new IllegalArgumentException();
    }

    public Transaction getTransaction(String uuidTransaction) {
        Transaction transaction;
        Optional<Transaction> foundTransaction = transactionRepository.findById(uuidTransaction);
        if (foundTransaction.isPresent())
            transaction = foundTransaction.get();
        else
            throw new ThereIsNoSuchTransactionException("There is no such transaction");

        return transaction;
    }

    private Transaction createNewTransaction(Account account, Integer transactionAmount) {
        Integer amountBefore = account.getBalance();

        account.setBalance(amountBefore + transactionAmount);

        return transactionRepository.save(
                new Transaction(account, null, TypeTransactionEnum.TRANSFER,
                        transactionAmount, new Date(), amountBefore, amountBefore + transactionAmount
                ));
    }

    private Transaction createNewTransaction(Card card, Integer transactionAmount) {
        Account account = card.getAccount();
        Integer amountBefore = account.getBalance();

        account.setBalance(amountBefore + transactionAmount);

        return transactionRepository.save(
                new Transaction(account, card, TypeTransactionEnum.TRANSFER,
                        transactionAmount, new Date(), amountBefore, amountBefore + transactionAmount
                ));
    }

    private List<Transaction> transferA2A(TransferRequestBody transferRequestBody) {
        List<Transaction> transactions = new LinkedList<>();

        Transaction transactionPayer = createNewTransaction(accountService.getAccount(transferRequestBody.getUuidPayer()),
                -transferRequestBody.getAmount()
        );

        Transaction transactionPayee = createNewTransaction(accountService.getAccount(transferRequestBody.getUuidPayee()),
                transferRequestBody.getAmount()
        );

        transactionPayer.setLinkedTransaction(transactionPayee);
        transactionPayee.setLinkedTransaction(transactionPayer);

        transactionRepository.save(transactionPayer);
        transactionRepository.save(transactionPayee);

        transactions.add(transactionPayer);
        transactions.add(transactionPayee);

        return transactions;
    }

    private List<Transaction> transferC2C(TransferRequestBody transferRequestBody) {
        List<Transaction> transactions = new LinkedList<>();

        Transaction transactionPayer = createNewTransaction(cardService.getCard(transferRequestBody.getUuidPayer()),
                -transferRequestBody.getAmount()
        );

        Transaction transactionPayee = createNewTransaction(cardService.getCard(transferRequestBody.getUuidPayee()),
                transferRequestBody.getAmount()
        );

        transactionPayer.setLinkedTransaction(transactionPayee);
        transactionPayee.setLinkedTransaction(transactionPayer);

        transactionRepository.save(transactionPayer);
        transactionRepository.save(transactionPayee);

        transactions.add(transactionPayer);
        transactions.add(transactionPayee);

        return transactions;
    }
}