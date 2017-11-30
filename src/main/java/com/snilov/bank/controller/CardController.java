package com.snilov.bank.controller;

import com.snilov.bank.service.CardService;
import com.snilov.bank.requestBody.CreateCardRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RepositoryRestController
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(value = "/cards")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public PersistentEntityResource createNewCard(@Valid @RequestBody CreateCardRequestBody createCardRequestBody, PersistentEntityResourceAssembler asm) {
        System.out.println(createCardRequestBody);
        return asm.toFullResource(cardService.createNewCard(createCardRequestBody));
    }

    @PutMapping(value = "/cards/{uuidCard}/blocking")
    @ResponseBody
    public PersistentEntityResource blockingCard(@PathVariable String uuidCard, PersistentEntityResourceAssembler asm) {
        System.out.println("blockingCard = " + uuidCard);
        return asm.toFullResource(cardService.blockingCard(uuidCard));
    }

    @PutMapping(value = "/cards/{uuidCard}/unblocking")
    @ResponseBody
    public PersistentEntityResource unblockedCard(@PathVariable String uuidCard, PersistentEntityResourceAssembler asm) {
        System.out.println("unblockingCard = " + uuidCard);
        return asm.toFullResource(cardService.unblockingCard(uuidCard));
    }
}
