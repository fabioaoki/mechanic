package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderAddressException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderAddressException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
