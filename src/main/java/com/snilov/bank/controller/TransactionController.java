package com.snilov.bank.controller;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.requestBody.TransactionRequestBody;
import com.snilov.bank.requestBody.TransferRequestBody;
import com.snilov.bank.service.TransactionService;
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

    @PostMapping(value = "/transactions/transfer")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Resources<PersistentEntityResource> transfer(@RequestBody @Valid TransferRequestBody transferRequestBody,
                                                        PersistentEntityResourceAssembler asm) {
        System.out.println("transfer = " + transferRequestBody);

        List<Transaction> transactions = transactionService.transfer(transferRequestBody);

        List<PersistentEntityResource> resources = new ArrayList<>();
        for (Transaction transaction : transactions) {
            PersistentEntityResource persistentEntityResource = asm.toFullResource(transaction);
            resources.add(persistentEntityResource);
        }

        return new Resources<>(resources);
    }
}
