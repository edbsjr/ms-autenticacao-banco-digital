package br.com.bancodigital.msautenticacao.adapter.in.web.controller;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.LoginMapper;
import br.com.bancodigital.msautenticacao.application.port.in.AuthenticateUserPort;
import br.com.bancodigital.msautenticacao.application.service.AuthService;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final LoginMapper loginMapper; // Injeta o mapper
    private final AuthenticateUserPort authenticateUserPort;

    public AuthController (LoginMapper loginMapper, AuthenticateUserPort authenticateUserPort){
        this.loginMapper = loginMapper;
        this.authenticateUserPort = authenticateUserPort;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginCommand loginCommand = loginMapper.toLoginCommand(loginRequest);

        LoginResponse loginResponse = loginMapper.toLoginResponse(authenticateUserPort.authenticate(loginCommand));

        return ResponseEntity.ok(loginResponse);
    }

}
