package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class TypeServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public TypeServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
