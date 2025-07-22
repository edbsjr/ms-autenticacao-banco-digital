package br.com.bancodigital.msautenticacao.adapter.in.web.exception.handler;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.ErrorResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.ErrorMapper;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAutenticacaoException(AuthenticationException ex){
        HttpStatus status = ex.getErrorCode().getHttpStatus();

        ErrorResponse errorResponse = ErrorMapper.fromErrorCode(ex.getErrorCode());

        return new  ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class) // Captura qualquer outra exceção não tratada especificamente
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Logar a exceção completa aqui é crucial em um ambiente real para depuração!
        // ex.printStackTrace(); // Ou use um logger (ex: slf4j)
        System.err.println("Erro inesperado: " + ex.getMessage());
        ex.printStackTrace(); // Apenas para ver no console do teste

        // Para erros genéricos, você pode definir um código padrão e uma mensagem genérica de 500
        // Ou, se você tiver um AuthenticationErrorCode para "Erro Interno", pode usá-lo.
        // Por exemplo, podemos criar um "GENERIC_ERROR" em AuthenticationErrorCode, se fizer sentido para você.
        // Ou criar um ErrorResponse diretamente aqui.

        // Vamos criar um ErrorResponse genérico para 500
        ErrorResponse errorResponse = ErrorMapper.fromHttpStatusAndMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred. Please try again later." // Mensagem mais amigável e segura
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
