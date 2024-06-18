package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class CompletedServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public CompletedServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
