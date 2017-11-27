package com.snilov.bank.card;


import com.snilov.bank.account.Account;
import com.snilov.bank.account.Account.Currency;
import com.snilov.bank.account.AccountRepository;
import com.snilov.bank.exception.ThereIsNoSuchAccountException;
import com.snilov.bank.exception.ThereIsNoSuchCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.snilov.bank.account.Account.*;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public CardService(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public Card createNewCard(CreateCardRequestBody createCardRequestBody) {
        Account account;
        Card card = new Card(createCardRequestBody.getNumber(), createCardRequestBody.getType(), createCardRequestBody.getBlocked());
        String uuid = createCardRequestBody.getAccountUuid();

        if (uuid != null && !uuid.isEmpty()) {
            Optional<Account> foundAccount = accountRepository.findById(uuid);
            if (foundAccount.isPresent())
                account = foundAccount.get();
            else
                throw new ThereIsNoSuchAccountException("There is no such account");
        } else {
            account = new Account(Currency.RUR, 0, AccountType.DEBIT);
        }

        card.setAccount(accountRepository.save(account));

        return cardRepository.save(card);
    }

    public Card blockedCard(String uuidCard) {
        Card card = getCard(uuidCard);

        if (!card.getBlocked()) {
            card.setBlocked(true);
            cardRepository.save(card);
        }

        return card;
    }

    public Card unblockedCard(String uuidCard) {
        Card card = getCard(uuidCard);

        if (card.getBlocked()) {
            card.setBlocked(false);
            cardRepository.save(card);
        }

        return card;
    }

    private Card getCard(String uuidCard) {
        Card card;
        Optional<Card> foundCard = cardRepository.findById(uuidCard);
        if (foundCard.isPresent())
            card = foundCard.get();
        else
            throw new ThereIsNoSuchCardException("There is no such card");

        return card;
    }
}
