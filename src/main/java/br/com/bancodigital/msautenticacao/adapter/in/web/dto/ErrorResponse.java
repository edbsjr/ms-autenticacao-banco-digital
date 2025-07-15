package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final int httpStatus;
    private final LocalDateTime timestamp;

    public ErrorResponse (String code, String message, int httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now(); // Pega o momento da criaçáo do erro
    }

    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
    public int getHttpStatus(){
        return httpStatus;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
