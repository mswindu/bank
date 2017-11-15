package com.snilov.bank.transaction;

import com.snilov.bank.account.Account;
import com.snilov.bank.card.Card;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account account;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Card card;

    @Column(nullable = false)
    private Integer transactionAmount;

    @Column(nullable = false)
    private Date transactionDate;

    @Column(nullable = false)
    private Integer amountBefore;

    @Column(nullable = false)
    private Integer amountAfter;

    public Transaction() {}

    public Transaction(Account account, Card card, Integer transactionAmount, Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getAmountBefore() {
        return amountBefore;
    }

    public void setAmountBefore(Integer amountBefore) {
        this.amountBefore = amountBefore;
    }

    public Integer getAmountAfter() {
        return amountAfter;
    }

    public void setAmountAfter(Integer amountAfter) {
        this.amountAfter = amountAfter;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "uuid='" + uuid + '\'' +
                ", account=" + account +
                ", transactionAmount=" + transactionAmount +
                ", transactionDate=" + transactionDate +
                ", amountBefore=" + amountBefore +
                ", amountAfter=" + amountAfter +
                '}';
    }
}
