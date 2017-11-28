package com.snilov.bank.exception;

public class CardIsBlockedException extends RuntimeException {
    public CardIsBlockedException(String message) {
        super(message);
    }

    public CardIsBlockedException() {
        super();
    }
}
