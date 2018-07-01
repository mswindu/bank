package com.snilov.bank.model;

import com.snilov.bank.model.enums.TypeTransactionEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Account account;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Card card;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Transaction linkedTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransactionEnum typeTransaction;

    @Column(nullable = false)
    private Integer transactionAmount;

    @Column(nullable = false)
    private Date transactionDate;

    @Column(nullable = false)
    private Integer amountBefore;

    @Column(nullable = false)
    private Integer amountAfter;

    @Column
    private Boolean isCanceled;

    @Column
    private Date cancellationDate;

    public Transaction(Account account, Card card, Transaction linkedTransaction, TypeTransactionEnum typeTransaction,
                       Integer transactionAmount, Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.linkedTransaction = linkedTransaction;
        this.typeTransaction = typeTransaction;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
        this.isCanceled = false;
    }

    public Transaction(Account account, Card card, TypeTransactionEnum typeTransaction, Integer transactionAmount,
                       Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.typeTransaction = typeTransaction;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
        this.isCanceled = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getCard(), that.getCard()) &&
                Objects.equals(getLinkedTransaction(), that.getLinkedTransaction()) &&
                getTypeTransaction() == that.getTypeTransaction() &&
                Objects.equals(getTransactionAmount(), that.getTransactionAmount()) &&
                Objects.equals(getTransactionDate(), that.getTransactionDate()) &&
                Objects.equals(getAmountBefore(), that.getAmountBefore()) &&
                Objects.equals(getAmountAfter(), that.getAmountAfter()) &&
                Objects.equals(getIsCanceled(), that.getIsCanceled()) &&
                Objects.equals(getCancellationDate(), that.getCancellationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getUuid(),
                getAccount(),
                getCard(),
                getLinkedTransaction(),
                getTypeTransaction(),
                getTransactionAmount(),
                getTransactionDate(),
                getAmountBefore(),
                getAmountAfter(),
                getIsCanceled(),
                getCancellationDate());
    }
}
