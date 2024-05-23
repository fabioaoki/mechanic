package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderAccountException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderAccountException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
