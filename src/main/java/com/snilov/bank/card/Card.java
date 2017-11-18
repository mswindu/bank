package com.snilov.bank.card;

import com.snilov.bank.account.Account;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Card implements Serializable {

    public enum TypeCard {DEBIT, CREDIT}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false)
    private String uuid;

    @Column(length = 16, nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCard type;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @GeneratedValue
    private Account account;

    @Column(nullable = false)
    private Boolean blocked;

    public Card(String number, TypeCard type, Boolean blocked) {
        this.number = number;
        this.type = type;
        this.blocked = blocked;
    }
}
