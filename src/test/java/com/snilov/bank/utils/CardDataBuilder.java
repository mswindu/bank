package com.snilov.bank.utils;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.Card;
import com.snilov.bank.model.enums.TypeCardEnum;
import com.snilov.bank.repository.CardRepository;

public class CardDataBuilder {

    private final CardRepository cardRepository;

    private Card card;

    public CardDataBuilder(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.card = Card.builder().build();
    }

    public CardDataBuilder number(String number) {
        this.card.setPan(number);
        return this;
    }

    public CardDataBuilder type(TypeCardEnum type) {
        this.card.setType(type);
        return this;
    }

    public CardDataBuilder blocked(Boolean blocked) {
        this.card.setBlocked(blocked);
        return this;
    }

    public CardDataBuilder account(Account account) {
        this.card.setAccount(account);
        return this;
    }

    public Card build() {
        return this.card;
    }

    public Card save() {
        return this.cardRepository.save(this.card);
    }
}
