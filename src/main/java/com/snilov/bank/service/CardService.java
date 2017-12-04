package com.snilov.bank.service;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.requestBody.CreateCardRequestBody;
import com.snilov.bank.model.Card;
import com.snilov.bank.exception.ThereIsNoSuchCardException;
import com.snilov.bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

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
            account = accountService.getAccount(uuid);
        } else {
            account = new Account(CurrencyEnum.RUR, 0, AccountTypeEnum.DEBIT);
        }

        card.setAccount(accountRepository.save(account));

        return cardRepository.save(card);
    }

    public Card blockingCard(String uuidCard) {
        Card card = getCard(uuidCard);

        if (!card.getBlocked()) {
            card.setBlocked(true);
            cardRepository.save(card);
        }

        return card;
    }

    public Card unblockingCard(String uuidCard) {
        Card card = getCard(uuidCard);

        if (card.getBlocked()) {
            card.setBlocked(false);
            cardRepository.save(card);
        }

        return card;
    }

    public Card getCard(String uuidCard) {
        Card card;
        Optional<Card> foundCard = cardRepository.findById(uuidCard);
        if (foundCard.isPresent())
            card = foundCard.get();
        else
            throw new ThereIsNoSuchCardException("There is no such card");

        return card;
    }
}
