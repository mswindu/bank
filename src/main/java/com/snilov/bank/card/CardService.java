package com.snilov.bank.card;


import com.snilov.bank.account.Account;
import com.snilov.bank.account.Account.Currency;
import com.snilov.bank.account.AccountRepository;
import com.snilov.bank.account.ThereIsNoSuchAccountException;
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

    public Card createNewCard(Card card) {
        Account account = card.getAccount();

        if(account != null){
            String uuid = account.getUuid();
            Optional<Account> foundAccount = accountRepository.findById(uuid);
            if (foundAccount.isPresent())
                account = foundAccount.get();
            else
                throw new ThereIsNoSuchAccountException();
        } else {
            account = new Account(Currency.RUR, 0, AccountType.DEBIT);
        }

        card.setAccount(accountRepository.save(account));

        return cardRepository.save(card);
    }
}
