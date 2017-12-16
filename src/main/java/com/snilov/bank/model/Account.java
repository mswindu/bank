package com.snilov.bank.model;

import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Account {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Currency cannot be empty")
    private CurrencyEnum currency;

    @Column(nullable = false)
    @NotNull(message = "Balance cannot be empty")
    private Integer balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Account type cannot be empty")
    private AccountTypeEnum type;

    public Account(CurrencyEnum currency, Integer balance, AccountTypeEnum type) {
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
