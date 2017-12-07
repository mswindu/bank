package com.snilov.bank.recource;

import com.snilov.bank.controller.CardController;
import com.snilov.bank.model.Card;
import com.snilov.bank.model.enums.TypeCardEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@Getter
public class CardRecourse extends ResourceSupport {
    private String uuid;
    private String number;
    private TypeCardEnum type;
    private Boolean blocked;

    public CardRecourse(Card card) {
        this.uuid = card.getUuid();
        this.number = card.getNumber();
        this.type = card.getType();
        this.blocked = card.getBlocked();
        //add(linkTo(methodOn(CardController.class).getCard(card.getUuid())).withSelfRel());

        if (blocked)
            add(linkTo(methodOn(CardController.class).unblockedCard(uuid)).withRel("unblocking"));
        else
            add(linkTo(methodOn(CardController.class).blockingCard(uuid)).withRel("blocking"));
    }
}
