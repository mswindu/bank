package com.snilov.bank.account;

import javax.persistence.*;

@Entity
public class Account {

    public enum AccountType {DEBIT, CREDIT}
    public enum Currency {RUR, EUR, USD}

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private Integer balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    public Account() {}

    public Account(Currency currency, Integer balance, AccountType type) {;
        this.currency = currency;
        this.balance = balance;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", type=" + type +
                '}';
    }
}
