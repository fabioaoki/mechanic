package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ClientPhoneException extends RuntimeException {
    private final ErrorCode errorCode;

    public ClientPhoneException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
