package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
