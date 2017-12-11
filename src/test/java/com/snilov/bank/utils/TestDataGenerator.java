package com.snilov.bank.utils;

import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDataGenerator {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    public AccountDataBuilder account() {
        return new AccountDataBuilder(accountRepository);
    }

    public CardDataBuilder card() {
        return new CardDataBuilder(cardRepository);
    }
}
