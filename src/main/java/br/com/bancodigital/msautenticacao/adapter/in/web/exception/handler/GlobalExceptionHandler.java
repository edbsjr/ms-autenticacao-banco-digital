package br.com.bancodigital.msautenticacao.adapter.in.web.exception.handler;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.ErrorResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.ErrorMapper;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorResponse.ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        log.warn("Erro de validação capturado para {} campos", validationErrors.size());


        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION-001",
                "Dados de entrada inválidos",
                HttpStatus.BAD_REQUEST.value(),
                validationErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn("Erro de leitura de JSON: {}", ex.getMessage());

        String genericMessage = "Erro na leitura do corpo da requisição. Verifique o formato do JSON ou valores de campos específicos (como perfis de acesso).";

        // Se quiser ser muito específico para Enums:
        if (ex.getMessage().contains("UserRole")) {
            genericMessage = "O perfil (role) informado é inválido. Valores aceitos: CLIENTE, GERENTE, SUPERVISOR.";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "MALFORMED-JSON-001",
                genericMessage,
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
