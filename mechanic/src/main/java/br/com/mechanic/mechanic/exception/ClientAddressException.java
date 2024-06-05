package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ClientAddressException extends RuntimeException {
    private final ErrorCode errorCode;

    public ClientAddressException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
