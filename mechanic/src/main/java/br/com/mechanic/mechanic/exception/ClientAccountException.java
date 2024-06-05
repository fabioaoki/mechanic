package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ClientAccountException extends RuntimeException {
    private final ErrorCode errorCode;

    public ClientAccountException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
