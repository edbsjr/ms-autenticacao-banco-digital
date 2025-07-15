package br.com.bancodigital.msautenticacao.domain.exception;

import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AutenticacaoErrorCode;

public class AutenticacaoException extends RuntimeException {

    private final AutenticacaoErrorCode errorCode; // Campo para armazenar o código de erro

    public AutenticacaoException(AutenticacaoErrorCode errorCode) {
        super(errorCode.getMensagem());
        this.errorCode = errorCode;
    }

    public AutenticacaoException(AutenticacaoErrorCode errorCode, Throwable cause) {
        // Chama o construtor da superclasse (RuntimeException) com a mensagem do erro code e a causa.
        super(errorCode.getMensagem(), cause);
        this.errorCode = errorCode;
    }

    /**
     * Retorna o AutenticacaoErrorCode associado a esta exceção.
     *
     * @return O AutenticacaoErrorCode que detalha o erro.
     */
    public AutenticacaoErrorCode getErrorCode() {
        //Retorna o ErrorCode associado
        return errorCode;
    }
}

