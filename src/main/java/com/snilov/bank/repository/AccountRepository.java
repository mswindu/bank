package com.snilov.bank.repository;

import com.snilov.bank.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface AccountRepository extends CrudRepository<Account, String> {

    @Override
    @RestResource(exported = false)
    void delete(Account entity);
}