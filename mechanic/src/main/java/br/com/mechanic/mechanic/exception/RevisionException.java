package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class RevisionException extends RuntimeException {
    private final ErrorCode errorCode;

    public RevisionException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
