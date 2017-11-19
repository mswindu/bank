package com.snilov.bank.exception;

public class ThereIsNoSuchAccountException extends RuntimeException {
    public ThereIsNoSuchAccountException(String message) {
        super(message);
    }

    public ThereIsNoSuchAccountException() {
        super();
    }
}