package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

// O @JsonInclude garante que a lista 'errors' não apareça no JSON se for nula
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int httpStatus;
    private final LocalDateTime timestamp;
    private final List<ValidationError> errors; // O novo campo

    // Construtor para erros comuns (sem lista de campos)
    public ErrorResponse(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
        this.errors = null;
    }

    // Construtor para erros de validação (com lista de campos)
    public ErrorResponse(String code, String message, int httpStatus, List<ValidationError> errors) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    // Record interno para os detalhes do campo
    public record ValidationError(String field, String message) {}

    // Getters
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public int getHttpStatus() { return httpStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<ValidationError> getErrors() { return errors; }
}