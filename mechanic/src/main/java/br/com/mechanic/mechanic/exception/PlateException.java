package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class PlateException extends RuntimeException {
    private final ErrorCode errorCode;

    public PlateException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
