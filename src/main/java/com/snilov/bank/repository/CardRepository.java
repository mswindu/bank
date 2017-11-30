package com.snilov.bank.repository;

import com.snilov.bank.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface CardRepository extends CrudRepository<Card, String> {

    @Override
    @RestResource(exported = false)
    void delete(Card entity);
}
