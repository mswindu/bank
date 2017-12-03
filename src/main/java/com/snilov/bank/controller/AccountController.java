package com.snilov.bank.controller;

import com.snilov.bank.entity.Card;
import com.snilov.bank.service.AccountService;
import com.snilov.bank.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RepositoryRestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/accounts")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public PersistentEntityResource createNewAccount(@Valid @RequestBody Account account, PersistentEntityResourceAssembler asm) {
        System.out.println(account);
        return asm.toFullResource(accountService.createNewAccount(account));
    }

    @GetMapping(value = "/accounts/{uuidAccount}/findCard")
    @ResponseBody
    public Resources<PersistentEntityResource> findCards(@PathVariable String uuidAccount, PersistentEntityResourceAssembler asm) {
        System.out.println(uuidAccount);
        List<Card> cards = accountService.findCards(uuidAccount);

        List<PersistentEntityResource> resources = new ArrayList<>();
        for (Card card : cards) {
            PersistentEntityResource persistentEntityResource = asm.toFullResource(card);
            resources.add(persistentEntityResource);
        }
        return new Resources<>(resources);
    }
}
