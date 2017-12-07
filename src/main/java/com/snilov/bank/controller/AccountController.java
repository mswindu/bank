package com.snilov.bank.controller;

import com.snilov.bank.model.Card;
import com.snilov.bank.recource.AccountResource;
import com.snilov.bank.recource.CardRecourse;
import com.snilov.bank.service.AccountService;
import com.snilov.bank.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@ExposesResourceFor(value = AccountResource.class)
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/accounts")
    @ResponseBody
    public ResponseEntity<AccountResource> createNewAccount(@Valid @RequestBody Account account) {
        System.out.println("createNewAccount " + account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResource(accountService.createNewAccount(account)));
    }

    @GetMapping(value = "/accounts/{uuidAccount}")
    @ResponseBody
    public ResponseEntity<AccountResource> getAccount(@PathVariable String uuidAccount) {
        System.out.println("getAccount = " + uuidAccount);
        return ResponseEntity.ok(new AccountResource(accountService.getAccount(uuidAccount)));
    }

    @GetMapping(value = "/accounts/{uuidAccount}/findCard")
    @ResponseBody
    public Resources<CardRecourse> findCards(@PathVariable String uuidAccount) {
        System.out.println("findCards " + uuidAccount);
        List<Card> cards = accountService.findCards(uuidAccount);

        List<CardRecourse> resources = new ArrayList<>();
        for (Card card : cards) {
            CardRecourse cardRecourse = new CardRecourse(card);
            resources.add(cardRecourse);
        }
        return new Resources<>(resources);
    }
}
