package com.snilov.bank.transaction;

import com.snilov.bank.account.AccountRepository;
import com.snilov.bank.exception.ThereIsNoSuchAccountException;
import com.snilov.bank.card.Card;
import com.snilov.bank.card.CardRepository;
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

        Integer transactionAmount = transactionRequestBody.getTransactionAmount();

        if (transactionRequestBody.getTypeTransaction() == Transaction.TypeTransaction.WITHDRAW)
            transactionAmount *= -1;

        Integer amountBefore = card.getAccount().getBalance();

        card.getAccount().setBalance(amountBefore + transactionAmount);
        accountRepository.save(card.getAccount());

        return transactionRepository.save(
                new Transaction(card.getAccount(), card, transactionRequestBody.getTypeTransaction(),
                        transactionRequestBody.getTransactionAmount(), new Date(), amountBefore,
                        amountBefore + transactionAmount
                ));
    }
}