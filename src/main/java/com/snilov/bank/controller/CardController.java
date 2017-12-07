package com.snilov.bank.controller;

import com.snilov.bank.recource.CardRecourse;
import com.snilov.bank.service.CardService;
import com.snilov.bank.requestBody.CreateCardRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ExposesResourceFor(value = CardRecourse.class)
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(value = "/cards")
    @ResponseBody
    public ResponseEntity<CardRecourse> createNewCard(@Valid @RequestBody CreateCardRequestBody createCardRequestBody) {
        System.out.println(createCardRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CardRecourse(cardService.createNewCard(createCardRequestBody)));
    }

    @PutMapping(value = "/cards/{uuidCard}/blocking")
    @ResponseBody
    public ResponseEntity<CardRecourse> blockingCard(@PathVariable String uuidCard) {
        System.out.println("blockingCard = " + uuidCard);
        return ResponseEntity.ok(new CardRecourse(cardService.blockingCard(uuidCard)));
    }

    @PutMapping(value = "/cards/{uuidCard}/unblocking")
    @ResponseBody
    public ResponseEntity<CardRecourse> unblockedCard(@PathVariable String uuidCard) {
        System.out.println("unblockingCard = " + uuidCard);
        return ResponseEntity.ok(new CardRecourse(cardService.unblockingCard(uuidCard)));
    }
}
