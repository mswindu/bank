package com.snilov.bank.model;

import com.snilov.bank.model.enums.TypeCardEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Card {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Column(length = 16, nullable = false, unique = true)
    private String pan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCardEnum type;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @GeneratedValue
    private Account account;

    @Column(nullable = false)
    private Boolean blocked;

    public Card(String pan, TypeCardEnum type, Boolean blocked) {
        this.pan = pan;
        this.type = type;
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (!getUuid().equals(card.getUuid())) return false;
        if (!getPan().equals(card.getPan())) return false;
        if (getType() != card.getType()) return false;
        if (!getAccount().equals(card.getAccount())) return false;
        return getBlocked().equals(card.getBlocked());
    }

    @Override
    public int hashCode() {
        int result = getUuid().hashCode();
        result = 31 * result + getPan().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getAccount().hashCode();
        result = 31 * result + getBlocked().hashCode();
        return result;
    }
}
