package com.snilov.bank.controller;

import com.snilov.bank.service.AccountService;
import com.snilov.bank.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public PersistentEntityResource createNewCard(@Valid @RequestBody Account account, PersistentEntityResourceAssembler asm) {
        System.out.println(account);
        return asm.toFullResource(accountService.createNewAccount(account));
    }
}
