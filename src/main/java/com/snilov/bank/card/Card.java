package com.snilov.bank.card;

import com.snilov.bank.account.Account;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
public class Card {

    public enum TypeCard {DEBIT, CREDIT}

    @Id
    @Column(length = 36)
    private String uuid = UUID.randomUUID().toString();

    @Column(length = 16)
    private String number;

    @Enumerated(EnumType.STRING)
    private TypeCard type;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @GeneratedValue
    private Account account;

    @Column(nullable = false)
    private Boolean blocked;

    // Так сделал, чтобы при создании новой карты, создавался новый счет.
    public Card() {
        this.account = new Account(Account.Currency.RUR, 0, Account.AccountType.DEBIT);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
                "uuid='" + uuid + '\'' +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", account=" + account +
                ", blocked=" + blocked +
                '}';
    }
}
