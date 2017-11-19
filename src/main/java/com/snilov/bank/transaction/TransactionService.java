package com.snilov.bank.transaction;

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

    private final CardRepository cardRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction createNewTransaction(String cardUuid, TransactionRequestBody transactionRequestBody) {
        Card card;
        Optional<Card> foundCard = cardRepository.findById(cardUuid);
        if (foundCard.isPresent())
            card = foundCard.get();
        else
            throw new ThereIsNoSuchAccountException("There is no such account");

        Integer transactionAmount = transactionRequestBody.getTransactionAmount();
        Integer amountBefore = card.getAccount().getBalance();

        return transactionRepository.save(new Transaction(card.getAccount(), card, transactionAmount, new Date(), amountBefore, amountBefore + transactionAmount));
    }
}
