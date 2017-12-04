package com.snilov.bank.repository;

import com.snilov.bank.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, String> {

    @Override
    @RestResource(exported = false)
    void delete(Card entity);

    List<Card> findCardsByAccount_Uuid(String uuidAccount);
}
