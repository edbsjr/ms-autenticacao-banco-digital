package br.com.bancodigital.msautenticacao.adapter.in.web.mapper;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.RegisterRequest;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.application.usecase.command.RegisterUserCommand;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {
    public RegisterUserCommand toRegisterCommand(RegisterRequest request) {
        return new RegisterUserCommand(
                request.username(),
                request.password(),
                request.role()
        );
    }
}
