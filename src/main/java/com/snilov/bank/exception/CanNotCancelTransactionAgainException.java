package com.snilov.bank.exception;

public class CanNotCancelTransactionAgainException extends RuntimeException {
    public CanNotCancelTransactionAgainException(String message) {
        super(message);
    }

    public CanNotCancelTransactionAgainException() {
        super();
    }
}
