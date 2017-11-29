package com.snilov.bank.transaction;

import com.snilov.bank.account.Account;
import com.snilov.bank.account.AccountRepository;
import com.snilov.bank.exception.CanNotCancelTransactionAgainException;
import com.snilov.bank.exception.CardIsBlockedException;
import com.snilov.bank.exception.ThereIsNoSuchAccountException;
import com.snilov.bank.card.Card;
import com.snilov.bank.card.CardRepository;
import com.snilov.bank.exception.ThereIsNoSuchTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    private final AccountRepository accountRepository;

    private final CardRepository cardRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createNewTransaction(TransactionRequestBody transactionRequestBody) {
        Card card;
        Optional<Card> foundCard = cardRepository.findById(transactionRequestBody.getUuidCard());
        if (foundCard.isPresent())
            card = foundCard.get();
        else
            throw new ThereIsNoSuchAccountException("There is no such card");

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

    public Transaction rollbackTransaction(String uuidTransaction) {
        Transaction transaction;
        Optional<Transaction> foundTransaction = transactionRepository.findById(uuidTransaction);
        if (foundTransaction.isPresent())
            transaction = foundTransaction.get();
        else
            throw new ThereIsNoSuchTransactionException("There is no such transaction");

        if (transaction.getLinkedTransaction() != null)
            throw new CanNotCancelTransactionAgainException("You can not cancel the transaction again");

        Integer amountBefore = transaction.getAccount().getBalance();

        Account account = transaction.getAccount();

        account.setBalance(amountBefore - transaction.getTransactionAmount());

        accountRepository.save(account);

        transaction.setCanceled(true);
        transactionRepository.save(transaction);

        return transactionRepository.save(new Transaction(transaction.getAccount(), transaction.getCard(), transaction, Transaction.TypeTransaction.ROLLBACK,
                -transaction.getTransactionAmount(), new Date(), amountBefore, amountBefore - transaction.getTransactionAmount()
        ));
    }
}