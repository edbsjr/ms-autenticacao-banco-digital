package br.com.bancodigital.msautenticacao.adapter.in.web.controller;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.LoginMapper;
import br.com.bancodigital.msautenticacao.application.port.in.AuthenticateUserPort;
import br.com.bancodigital.msautenticacao.application.service.AuthService;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
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
        log.info("Requisição POST /auth/login recebida, iniciando converção da request em command");
        if (log.isDebugEnabled()) {
            // Criando uma cópia segura para log
            String safeRequestPayload = String.format("{ \"username\": \"%s\", \"password\": \"[REDACTED]\" }",
                    loginRequest.getUsername());
            log.debug("Payload da requisição de login: {}", safeRequestPayload);
        }

        LoginCommand loginCommand = loginMapper.toLoginCommand(loginRequest);

        LoginResponse loginResponse = loginMapper.toLoginResponse(authenticateUserPort.authenticate(loginCommand));

        log.info("Login bem-sucedido, retornando token");
        return ResponseEntity.ok(loginResponse);
    }

}
