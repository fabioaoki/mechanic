package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class VehicleTypeException extends RuntimeException {
    private final ErrorCode errorCode;

    public VehicleTypeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
