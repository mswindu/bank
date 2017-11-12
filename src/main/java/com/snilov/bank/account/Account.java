package com.snilov.bank.account;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Account {

    public enum AccountType {DEBIT, CREDIT}
    public enum Currency {RUR, EUR, USD}

    @Id
    @Column(length = 36)
    private String uuid = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private Integer balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    public Account() {}

    public Account(Currency currency, Integer balance, AccountType type) {
        this.currency = currency;
        this.balance = balance;
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid='" + uuid + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", type=" + type +
                '}';
    }
}
