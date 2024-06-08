package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class ModelException extends RuntimeException {
    private final ErrorCode errorCode;

    public ModelException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
