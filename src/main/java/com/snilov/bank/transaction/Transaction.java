package com.snilov.bank.transaction;

import com.snilov.bank.account.Account;
import com.snilov.bank.card.Card;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
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

    public Transaction(Account account, Card card, Integer transactionAmount, Date transactionDate, Integer amountBefore, Integer amountAfter) {
        this.account = account;
        this.card = card;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
    }
}
