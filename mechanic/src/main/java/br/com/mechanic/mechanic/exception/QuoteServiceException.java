package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class QuoteServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public QuoteServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
