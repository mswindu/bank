package com.snilov.bank.controller;

import com.snilov.bank.requestBody.TransactionRequestBody;
import com.snilov.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RepositoryRestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/transactions")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public PersistentEntityResource createNewTransaction(@RequestBody @Valid TransactionRequestBody transactionRequestBody,
                                                         PersistentEntityResourceAssembler asm) {
        System.out.println(transactionRequestBody);

        return asm.toFullResource(transactionService.createNewTransaction(transactionRequestBody));
    }

    @PostMapping(value = "/transactions/{uuidTransaction}/rollback")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public PersistentEntityResource rollbackTransaction(@PathVariable String uuidTransaction, PersistentEntityResourceAssembler asm) {
        System.out.println("rollbackTransaction = " + uuidTransaction);
        return asm.toFullResource(transactionService.rollbackTransaction(uuidTransaction));
    }
}
