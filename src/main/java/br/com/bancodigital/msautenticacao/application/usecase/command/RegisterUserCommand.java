package br.com.bancodigital.msautenticacao.application.usecase.command;

import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;

public record RegisterUserCommand (String login, String password, UserRole role){}
