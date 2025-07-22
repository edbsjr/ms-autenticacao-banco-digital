package br.com.bancodigital.msautenticacao.application.port.in;

import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser; // Continua no domínio

public interface AuthenticateUserPort {
    AuthenticatedUser authenticate(LoginCommand command);
}
