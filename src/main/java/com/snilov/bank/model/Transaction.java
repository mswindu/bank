package com.snilov.bank.model;

import com.snilov.bank.model.enums.TypeTransactionEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
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
        if (!super.equals(o)) return false;

        Transaction that = (Transaction) o;

        if (!getUuid().equals(that.getUuid())) return false;
        if (!getAccount().equals(that.getAccount())) return false;
        if (!getCard().equals(that.getCard())) return false;
        if (getLinkedTransaction() != null ? !getLinkedTransaction().equals(that.getLinkedTransaction()) : that.getLinkedTransaction() != null)
            return false;
        if (getTypeTransaction() != that.getTypeTransaction()) return false;
        if (!getTransactionAmount().equals(that.getTransactionAmount())) return false;
        if (!getTransactionDate().equals(that.getTransactionDate())) return false;
        if (!getAmountBefore().equals(that.getAmountBefore())) return false;
        if (!getAmountAfter().equals(that.getAmountAfter())) return false;
        return getIsCanceled().equals(that.getIsCanceled());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getUuid().hashCode();
        result = 31 * result + getAccount().hashCode();
        result = 31 * result + (getCard() != null ? getCard().hashCode() : 0);
        result = 31 * result + (getLinkedTransaction() != null ? getLinkedTransaction().hashCode() : 0);
        result = 31 * result + getTypeTransaction().hashCode();
        result = 31 * result + getTransactionAmount().hashCode();
        result = 31 * result + getTransactionDate().hashCode();
        result = 31 * result + getAmountBefore().hashCode();
        result = 31 * result + getAmountAfter().hashCode();
        result = 31 * result + getIsCanceled().hashCode();
        return result;
    }
}
