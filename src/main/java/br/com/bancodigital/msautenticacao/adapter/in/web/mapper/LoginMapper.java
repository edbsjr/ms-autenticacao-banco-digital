package br.com.bancodigital.msautenticacao.adapter.in.web.mapper;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    //Converte LoginRequest em LoginCommand
    public LoginCommand toLoginCommand (LoginRequest loginRequest){
        return new LoginCommand(loginRequest.getUsername(), loginRequest.getPassword());
    }

    //Converte AuthenticatedUser em LoginResponse
    public LoginResponse toLoginResponse (AuthenticatedUser authenticatedUser){
        return new LoginResponse(authenticatedUser.token(), authenticatedUser.role());
    }
}
