package br.com.mechanic.mechanic.exception;

public class ErrorResponse {

    private ErrorCode errorCode; // Seu enum de erro
    private String message;      // Mensagem de erro

    // Construtor vazio
    public ErrorResponse() {
    }

    // Construtor com todos os campos
    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    // Getters e setters
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
