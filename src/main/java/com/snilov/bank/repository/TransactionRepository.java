package com.snilov.bank.repository;

import com.snilov.bank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    @Override
    @RestResource(exported = false)
    void delete(Transaction entity);
}
