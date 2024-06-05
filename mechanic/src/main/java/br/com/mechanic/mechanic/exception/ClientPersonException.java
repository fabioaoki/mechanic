package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ClientPersonException extends RuntimeException {
    private final ErrorCode errorCode;

    public ClientPersonException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
