package com.snilov.bank.recource;

import com.snilov.bank.controller.TransactionController;
import com.snilov.bank.model.Transaction;
import com.snilov.bank.model.enums.TypeTransactionEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@Getter
@Relation(value = "transaction", collectionRelation = "transactions")
public class TransactionResource extends ResourceSupport {

    private String uuid;
    private TypeTransactionEnum typeTransaction;
    private Integer transactionAmount;
    private Date transactionDate;
    private Integer amountBefore;
    private Integer amountAfter;
    private Boolean isCanceled;

    public TransactionResource(Transaction transaction) {
        this.uuid = transaction.getUuid();
        this.typeTransaction = transaction.getTypeTransaction();
        this.transactionAmount = transaction.getTransactionAmount();
        this.transactionDate = transaction.getTransactionDate();
        this.amountBefore = transaction.getAmountBefore();
        this.amountAfter = transaction.getAmountAfter();
        this.isCanceled = transaction.getIsCanceled();

        add(linkTo(methodOn(TransactionController.class).getTransaction(transaction.getUuid())).withSelfRel());

        if(!isCanceled)
            add(linkTo(methodOn(TransactionController.class).rollbackTransaction(transaction.getUuid())).withRel("rollback"));
    }
}
