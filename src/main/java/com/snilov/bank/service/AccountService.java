package com.snilov.bank.service;

import com.snilov.bank.entity.Account;
import com.snilov.bank.entity.Card;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountService {

    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    public Account createNewAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Card> findCards(String uuidAccount) {
        return cardRepository.findCardsByAccount_Uuid(uuidAccount);
    }
}
