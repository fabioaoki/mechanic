package br.com.mechanic.mechanic.exception;

import lombok.Getter;

@Getter
public class VehicleException extends RuntimeException {
    private final ErrorCode errorCode;

    public VehicleException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
