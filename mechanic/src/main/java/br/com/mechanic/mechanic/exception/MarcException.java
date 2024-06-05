package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class MarcException extends RuntimeException {
    private final ErrorCode errorCode;

    public MarcException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
