package com.snilov.bank.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/{cardUuid}")
    @ResponseBody
    public PersistentEntityResource createNewTransaction(@PathVariable String cardUuid, @RequestBody TransactionRequestBody transactionRequestBody, PersistentEntityResourceAssembler asm) {
        System.out.println("cardUuid: " + cardUuid);
        System.out.println(transactionRequestBody);

        return asm.toFullResource(transactionService.createNewTransaction(cardUuid, transactionRequestBody));
    }
}
