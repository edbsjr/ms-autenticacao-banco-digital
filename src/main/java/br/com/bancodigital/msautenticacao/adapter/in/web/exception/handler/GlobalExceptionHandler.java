package br.com.bancodigital.msautenticacao.adapter.in.web.exception.handler;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.ErrorResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.ErrorMapper;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAutenticacaoException(AuthenticationException ex){
        log.warn("Erro de negócio capturado: {} - {}", ex.getErrorCode().getCode(), ex.getMessage());
        HttpStatus status = ex.getErrorCode().getHttpStatus();

        ErrorResponse errorResponse = ErrorMapper.fromErrorCode(ex.getErrorCode());

        return new  ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Logamos como ERROR com o StackTrace, pois isso é um bug inesperado (NullPointer, Banco caiu, etc)
        log.error("Erro inesperado no sistema: ", ex);

        // Usa o Mapper para criar a resposta genérica de 500
        ErrorResponse errorResponse = ErrorMapper.fromHttpStatusAndMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
