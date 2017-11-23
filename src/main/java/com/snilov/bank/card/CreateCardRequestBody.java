package com.snilov.bank.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateCardRequestBody {
    @NotNull(message = "Number card cannot be empty")
    private String number;

    @NotNull(message = "Type card cannot be empty")
    private Card.TypeCard type;

    @NotNull(message = "Blocked status cannot be empty")
    private Boolean blocked;

    //@Length(min = 36, max = 36, message = "Account uuid must be 36 characters long")
    private String accountUuid;
}
