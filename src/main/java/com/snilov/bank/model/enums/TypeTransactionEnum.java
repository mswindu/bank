package com.snilov.bank.model.enums;

public enum TypeTransactionEnum {
    /**
     * Добавление средств на счет
     */
    DEPOSIT,

    /**
     * Уменьшение средств на счете
     */
    WITHDRAW,

    /**
     * Отмена транзакции
     */
    ROLLBACK,

    /**
     * Перевод средств с одного счета на другой
     */
    TRANSFER
}