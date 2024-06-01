package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderServiceIdentifierException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderServiceIdentifierException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
