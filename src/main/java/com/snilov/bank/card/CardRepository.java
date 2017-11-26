package com.snilov.bank.card;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface CardRepository extends CrudRepository<Card, String> {

    @Override
    @RestResource(exported = false)
    void delete(Card entity);
}
