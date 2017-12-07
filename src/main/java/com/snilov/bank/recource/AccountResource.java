package com.snilov.bank.recource;

import com.snilov.bank.controller.AccountController;
import com.snilov.bank.model.Account;
import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@Getter
@Relation(value = "account", collectionRelation = "accounts")
public class AccountResource extends ResourceSupport {

    private String uuid;
    private CurrencyEnum currency;
    private Integer balance;
    private AccountTypeEnum type;

    public AccountResource(Account account) {
        this.uuid = account.getUuid();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
        this.type = account.getType();

        add(linkTo(methodOn(AccountController.class).getAccount(account.getUuid())).withSelfRel());
    }

}
