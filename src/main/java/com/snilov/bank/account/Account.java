package com.snilov.bank.account;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Account {

    public enum AccountType {DEBIT, CREDIT}
    public enum Currency {RUR, EUR, USD}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Integer balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    public Account(Currency currency, Integer balance, AccountType type) {
        this.currency = currency;
        this.balance = balance;
        this.type = type;
    }
}
