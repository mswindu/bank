package com.snilov.bank.exception;

public class ThereIsNoSuchCardException extends RuntimeException {
    public ThereIsNoSuchCardException(String message) {
        super(message);
    }

    public ThereIsNoSuchCardException() {
        super();
    }
}
