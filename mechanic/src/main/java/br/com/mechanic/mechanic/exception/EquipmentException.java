package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class EquipmentException extends RuntimeException {
    private final ErrorCode errorCode;

    public EquipmentException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
