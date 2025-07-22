package br.com.bancodigital.msautenticacao.domain.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum AuthenticationErrorCode {

    // --- Códigos para Erros de Requisição Inválida (HTTP 400 Bad Request) ---
    // Geralmente indica que a entrada do cliente é inválida ou uma regra de negócio específica foi violada.
    USUARIO_OU_SENHA_INVALIDOS("AUTH-001", "Usuário ou senha inválidos. Verifique suas credenciais.", HttpStatus.BAD_REQUEST),
    CAMPO_OBRIGATORIO_AUSENTE("AUTH-002", "Um campo obrigatório não foi fornecido.", HttpStatus.BAD_REQUEST),
    DADOS_INVALIDOS("AUTH-003", "Os dados fornecidos são inválidos ou mal formatados.", HttpStatus.BAD_REQUEST),
    // Exemplo para um caso futuro: se você tivesse validação de formato de e-mail, por exemplo.
    FORMATO_EMAIL_INVALIDO("AUTH-004", "O formato do e-mail é inválido.", HttpStatus.BAD_REQUEST),


    // --- Códigos para Erros de Autenticação/Autorização (HTTP 401 Unauthorized / 403 Forbidden) ---
    TOKEN_AUSENTE("AUTH-005", "Token de autenticação não fornecido.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALIDO("AUTH-006", "Token de autenticação inválido.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRADO("AUTH-007", "Token de autenticação expirado.", HttpStatus.UNAUTHORIZED),
    ACESSO_NEGADO("AUTH-008", "Você não tem permissão para acessar este recurso.", HttpStatus.FORBIDDEN),


    // --- Códigos para Erros de Recurso Não Encontrado (HTTP 404 Not Found) ---
    USUARIO_NAO_ENCONTRADO("AUTH-009", "O usuário especificado não foi encontrado.", HttpStatus.NOT_FOUND),
    RECURSO_NAO_ENCONTRADO("AUTH-010", "O recurso solicitado não foi encontrado.", HttpStatus.NOT_FOUND),


    // --- Códigos para Erros de Conflito (HTTP 409 Conflict) ---
    // Geralmente para indicar que uma requisição não pode ser completada devido a um conflito com o estado atual do recurso.
    USUARIO_JA_EXISTE("AUTH-011", "Já existe um usuário com essas credenciais.", HttpStatus.CONFLICT),


    // --- Código para Erros Internos do Servidor (HTTP 500 Internal Server Error) ---
    // Deve ser um erro genérico para quando algo inesperado acontece no servidor.
    ERRO_INTERNO_SERVIDOR("AUTH-999", "Ocorreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code; // Nome da variável para o código do erro
    private final String message; // Nome da variável para a message descritiva do erro
    private final HttpStatus httpStatus; // O HttpStatus correspondente

    // Construtor do Enum
    AuthenticationErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    // Getters para acessar os valores do Enum
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
