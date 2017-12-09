package com.snilov.bank.controller;

import com.snilov.bank.model.Transaction;
import com.snilov.bank.recource.TransactionResource;
import com.snilov.bank.requestBody.TransactionRequestBody;
import com.snilov.bank.requestBody.TransferRequestBody;
import com.snilov.bank.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/transactions")
    @ResponseBody
    public ResponseEntity<TransactionResource> createNewTransaction(@RequestBody @Valid TransactionRequestBody transactionRequestBody) {
        log.debug("createNewTransaction = " + transactionRequestBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResource(transactionService.createNewTransaction(transactionRequestBody)));
    }

    @GetMapping(value = "/transactions/{uuidTransaction}")
    @ResponseBody
    public ResponseEntity<TransactionResource> getTransaction(@PathVariable String uuidTransaction) {
        log.debug("getAccount = " + uuidTransaction);

        return ResponseEntity.ok(new TransactionResource(transactionService.getTransaction(uuidTransaction)));
    }

    @PostMapping(value = "/transactions/{uuidTransaction}/rollback")
    @ResponseBody
    public ResponseEntity<TransactionResource> rollbackTransaction(@PathVariable String uuidTransaction) {
        log.debug("rollbackTransaction = " + uuidTransaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResource(transactionService.rollbackTransaction(uuidTransaction)));
    }

    @PostMapping(value = "/transactions/transfer")
    @ResponseBody
    public Resources<TransactionResource> transfer(@RequestBody @Valid TransferRequestBody transferRequestBody) {
        log.debug("transfer = " + transferRequestBody);

        List<Transaction> transactions = transactionService.transfer(transferRequestBody);

        List<TransactionResource> resources = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResource transactionResource = new TransactionResource(transaction);
            resources.add(transactionResource);
        }

        return new Resources<>(resources);
    }
}
