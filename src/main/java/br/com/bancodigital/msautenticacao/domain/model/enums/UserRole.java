package br.com.bancodigital.msautenticacao.domain.model.enums;

public enum UserRole
{
    CLIENTE ("Cliente"),
    GERENTE ("Gerente"),
    ADMIN ("Admin");

    private final String description;
    UserRole(String description) {this.description = description;}

    public String getDescription() {return description;}
}