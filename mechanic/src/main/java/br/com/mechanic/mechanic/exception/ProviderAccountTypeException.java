package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderAccountTypeException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderAccountTypeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
