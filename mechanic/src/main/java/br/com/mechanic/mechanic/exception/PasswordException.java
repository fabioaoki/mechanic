package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class PasswordException extends RuntimeException {
    private final ErrorCode errorCode;

    public PasswordException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
