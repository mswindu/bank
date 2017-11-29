package com.snilov.bank.transaction;

import com.snilov.bank.account.Account;
import com.snilov.bank.card.Card;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Transaction {

    public enum TypeTransaction {DEPOSIT, WITHDRAW, ROLLBACK}

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Account account;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Card card;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Transaction linkedTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;

    @Column(nullable = false)
    private Integer transactionAmount;

    @Column(nullable = false)
    private Date transactionDate;

    @Column(nullable = false)
    private Integer amountBefore;

    @Column(nullable = false)
    private Integer amountAfter;

    @Column
    private boolean isCanceled;

    public Transaction(Account account, Card card, Transaction linkedTransaction, TypeTransaction typeTransaction,
                       Integer transactionAmount, Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.linkedTransaction = linkedTransaction;
        this.typeTransaction = typeTransaction;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
    }

    public Transaction(Account account, Card card, TypeTransaction typeTransaction, Integer transactionAmount,
                       Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.typeTransaction = typeTransaction;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
    }
}
