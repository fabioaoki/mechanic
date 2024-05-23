package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ProviderPhoneException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProviderPhoneException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
