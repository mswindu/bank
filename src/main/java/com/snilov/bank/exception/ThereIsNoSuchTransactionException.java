package com.snilov.bank.exception;

public class ThereIsNoSuchTransactionException extends RuntimeException {
    public ThereIsNoSuchTransactionException(String message) {
        super(message);
    }

    public ThereIsNoSuchTransactionException() {
        super();
    }
}
