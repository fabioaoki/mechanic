package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ColorException extends RuntimeException {
    private final ErrorCode errorCode;

    public ColorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
