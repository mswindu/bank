package com.snilov.bank.controller;

import com.snilov.bank.constant.Path;
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
@RequestMapping(value = Path.TRANSACTIONS)
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TransactionResource> createNewTransaction(@RequestBody @Valid TransactionRequestBody transactionRequestBody) {
        log.debug("createNewTransaction = " + transactionRequestBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResource(transactionService.createNewTransaction(transactionRequestBody)));
    }

    @GetMapping(value = Path.TRANSACTION)
    @ResponseBody
    public ResponseEntity<TransactionResource> getTransaction(@PathVariable String uuidTransaction) {
        log.debug("getAccount = " + uuidTransaction);

        return ResponseEntity.ok(new TransactionResource(transactionService.getTransaction(uuidTransaction)));
    }

    @PostMapping(value = Path.TRANSACTION_ROLLBACK)
    @ResponseBody
    public Resources<TransactionResource> rollbackTransaction(@PathVariable String uuidTransaction) {
        log.debug("rollbackTransaction = " + uuidTransaction);

        List<Transaction> transactions = transactionService.rollbackTransaction(uuidTransaction);

        List<TransactionResource> resources = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResource transactionResource = new TransactionResource(transaction);
            resources.add(transactionResource);
        }

        return new Resources<>(resources);
    }

    @PostMapping(value = Path.TRANSFER)
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
