package com.snilov.bank.requestBody;

import com.snilov.bank.model.enums.TypeTransferEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TransferRequestBody {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type transfer cannot be empty")
    private TypeTransferEnum typeTransferEnum;

    /**
     * Плательщик
     */
    @NotNull(message = "Payer cannot be empty")
    private String uuidPayer;

    /**
     * Получатель
     */
    @NotNull(message = "Payee cannot be empty")
    private String uuidPayee;

    @NotNull(message = "Amount cannot be empty")
    private Integer amount;
}
