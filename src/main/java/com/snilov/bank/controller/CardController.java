package com.snilov.bank.controller;

import com.snilov.bank.constant.Path;
import com.snilov.bank.recource.CardRecourse;
import com.snilov.bank.service.CardService;
import com.snilov.bank.requestBody.CreateCardRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@ExposesResourceFor(value = CardRecourse.class)
@RequestMapping(value = Path.CARDS)
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CardRecourse> createNewCard(@Valid @RequestBody CreateCardRequestBody createCardRequestBody) {
        log.debug("createNewCard = " + createCardRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CardRecourse(cardService.createNewCard(createCardRequestBody)));
    }

    @GetMapping(value = Path.CARD)
    @ResponseBody
    public ResponseEntity<CardRecourse> getCard(@PathVariable String uuidCard) {
        log.debug("getCard = " + uuidCard);
        return ResponseEntity.ok(new CardRecourse(cardService.getCard(uuidCard)));
    }

    @PutMapping(value = Path.CARD_BLOCK)
    @ResponseBody
    public ResponseEntity<CardRecourse> blockCard(@PathVariable String uuidCard) {
        log.debug("blockCard = " + uuidCard);
        return ResponseEntity.ok(new CardRecourse(cardService.blockCard(uuidCard)));
    }

    @PutMapping(value = Path.CARD_UNBLOCK)
    @ResponseBody
    public ResponseEntity<CardRecourse> unblockCard(@PathVariable String uuidCard) {
        log.debug("unblockCard = " + uuidCard);
        return ResponseEntity.ok(new CardRecourse(cardService.unblockCard(uuidCard)));
    }
}
