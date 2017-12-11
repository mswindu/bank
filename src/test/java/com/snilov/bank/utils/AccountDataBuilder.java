package com.snilov.bank.utils;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import com.snilov.bank.repository.AccountRepository;

public class AccountDataBuilder {

    private final AccountRepository accountRepository;

    private Account account;

    public AccountDataBuilder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.account = Account.builder().build();
    }

    public AccountDataBuilder currency(CurrencyEnum currency) {
        this.account.setCurrency(currency);
        return this;
    }

    public AccountDataBuilder balance(Integer balance) {
        this.account.setBalance(balance);
        return this;
    }

    public AccountDataBuilder type(AccountTypeEnum type) {
        this.account.setType(type);
        return this;
    }

    public Account build() {
        return this.account;
    }

    public Account save() {
        return this.accountRepository.save(this.account);
    }
}
