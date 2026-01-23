package br.com.bancodigital.msautenticacao.application.port.in;

import br.com.bancodigital.msautenticacao.application.usecase.command.RegisterUserCommand;
import br.com.bancodigital.msautenticacao.domain.model.User;

public interface RegisterUserUseCase {
    User register(RegisterUserCommand registerUserCommand);
}
