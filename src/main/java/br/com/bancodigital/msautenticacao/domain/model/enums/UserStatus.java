package br.com.bancodigital.msautenticacao.domain.model.enums;

public enum UserStatus {

    ATIVO("Ativo"),
    BLOQUADO("Bloqueado"),
    DELETADO("Deletado");

    private final String description;

    UserStatus(String description) { this.description = description;}

    public String getDescription(){return description;}

}
