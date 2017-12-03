package com.snilov.bank.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Card implements Serializable {

    public enum TypeCard {DEBIT, CREDIT}

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Column(length = 16, nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCard type;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @GeneratedValue
    private Account account;

    @Column(nullable = false)
    private Boolean blocked;

    public Card(String number, TypeCard type, Boolean blocked) {
        this.number = number;
        this.type = type;
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (!getUuid().equals(card.getUuid())) return false;
        if (!getNumber().equals(card.getNumber())) return false;
        if (getType() != card.getType()) return false;
        if (!getAccount().equals(card.getAccount())) return false;
        return getBlocked().equals(card.getBlocked());
    }

    @Override
    public int hashCode() {
        int result = getUuid().hashCode();
        result = 31 * result + getNumber().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getAccount().hashCode();
        result = 31 * result + getBlocked().hashCode();
        return result;
    }
}