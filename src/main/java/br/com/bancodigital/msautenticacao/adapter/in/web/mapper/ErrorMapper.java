package br.com.bancodigital.msautenticacao.adapter.in.web.mapper;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.ErrorResponse;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AutenticacaoErrorCode;
import org.springframework.http.HttpStatus;

public class ErrorMapper {

    public static ErrorResponse fromErrorCode(AutenticacaoErrorCode errorCode){
        return new ErrorResponse(
                errorCode.getCodigo(),
                errorCode.getMensagem(),
                errorCode.getHttpStatus().value()
        );
    }

    //lida com exceptions genericas que nao vem de AutenticacaoException
    public static ErrorResponse fromHttpStatusAndMessage(HttpStatus status, String message){
        return new ErrorResponse(
                String.valueOf(status.value()),// Usa codigo HTTP como codigo interno
                message,
                status.value()
        );
    }
}
