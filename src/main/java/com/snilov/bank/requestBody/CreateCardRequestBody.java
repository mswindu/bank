package com.snilov.bank.requestBody;

import com.snilov.bank.entity.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
