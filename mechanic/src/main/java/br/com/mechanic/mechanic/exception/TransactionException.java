package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final ErrorCode errorCode;

    public TransactionException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
