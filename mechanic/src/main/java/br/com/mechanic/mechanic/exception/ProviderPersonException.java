package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderPersonException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderPersonException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
