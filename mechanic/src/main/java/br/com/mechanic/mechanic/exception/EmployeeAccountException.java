package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class EmployeeAccountException extends RuntimeException {
    private final ErrorCode errorCode;

    public EmployeeAccountException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
