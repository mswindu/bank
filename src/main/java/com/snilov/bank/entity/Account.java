package com.snilov.bank.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Account implements Serializable {

    public enum AccountType {DEBIT, CREDIT}

    public enum Currency {RUR, EUR, USD}

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Currency cannot be empty")
    private Currency currency;

    @Column(nullable = false)
    @NotNull(message = "Balance cannot be empty")
    private Integer balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Account type cannot be empty")
    private AccountType type;

    public Account(Currency currency, Integer balance, AccountType type) {
        this.currency = currency;
        this.balance = balance;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        if (!super.equals(o)) return false;

        Account account = (Account) o;

        if (!getUuid().equals(account.getUuid())) return false;
        if (getCurrency() != account.getCurrency()) return false;
        if (!getBalance().equals(account.getBalance())) return false;
        return getType() == account.getType();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getUuid().hashCode();
        result = 31 * result + getCurrency().hashCode();
        result = 31 * result + getBalance().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }
}
