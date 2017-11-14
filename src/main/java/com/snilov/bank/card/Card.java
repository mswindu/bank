package com.snilov.bank.card;

import com.snilov.bank.account.Account;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Card implements Serializable {

    public enum TypeCard {DEBIT, CREDIT}

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 16)
    private String number;

    @Enumerated(EnumType.STRING)
    private TypeCard type;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @GeneratedValue
    private Account account;

    @Column(nullable = false)
    private Boolean blocked;

    public Card() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TypeCard getType() {
        return type;
    }

    public void setType(TypeCard type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", account=" + account +
                ", blocked=" + blocked +
                '}';
    }
}
