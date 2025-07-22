package br.com.bancodigital.msautenticacao.domain.exception;

import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;

public class AuthenticationException extends RuntimeException {

    private final AuthenticationErrorCode errorCode; // Campo para armazenar o código de erro

    public AuthenticationException(AuthenticationErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AuthenticationException(AuthenticationErrorCode errorCode, Throwable cause) {
        // Chama o construtor da superclasse (RuntimeException) com a mensagem do erro code e a causa.
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * Retorna o AuthenticationErrorCode associado a esta exceção.
     *
     * @return O AuthenticationErrorCode que detalha o erro.
     */
    public AuthenticationErrorCode getErrorCode() {
        //Retorna o ErrorCode associado
        return errorCode;
    }
}

