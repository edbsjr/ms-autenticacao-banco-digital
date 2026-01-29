package br.com.bancodigital.msautenticacao.adapter.in.web.controller;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.RegisterRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.LoginMapper;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.RegisterMapper;
import br.com.bancodigital.msautenticacao.application.port.in.LoginUseCase;
import br.com.bancodigital.msautenticacao.application.port.in.RegisterUserUseCase;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.application.usecase.command.RegisterUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private final LoginMapper loginMapper;
    private final RegisterUserUseCase registerUserUseCase;
    private final RegisterMapper registerMapper;
    private final LoginUseCase loginUseCase;


    @Operation(
            summary = "Realiza o login do usuário",
            description = "Recebe as credenciais e retorna um token JWT válido para acesso aos demais recursos do sistema."
    )
    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos. Verifique suas credenciais.", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Requisição POST /auth/login recebida, iniciando conversão da request em command");
        if (log.isDebugEnabled()) {
            // Criando uma cópia segura para log
            String safeRequestPayload = String.format("{ \"username\": \"%s\", \"password\": \"[REDACTED]\" }",
                    loginRequest.username());
            log.debug("Payload da requisição de login: {}", safeRequestPayload);
        }

        LoginCommand loginCommand = loginMapper.toLoginCommand(loginRequest);

        LoginResponse loginResponse = loginMapper.toLoginResponse(loginUseCase.authenticate(loginCommand));

        log.info("Login bem-sucedido, retornando token");
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(
            summary = "Realiza o registro de novo usuário",
            description = "Cria um novo registro de usuário no banco de dados com senha criptografada."
    )
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "409", description = "Já existe um usuário com essas credenciais.")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest){
        log.info("Requisição POST /auth/register recebida, iniciando converção da request em command");
        if (log.isDebugEnabled()) {
            // Criando uma cópia segura para log
            String safeRequestPayload = String.format(
                    "{ \"username\": \"%s\", \"role\": \"%s\", \"password\": \"[REDACTED]\" }",
                    registerRequest.username(),
                    registerRequest.role()
            );
            log.debug("Payload da requisição de registro: {}", safeRequestPayload);
        }

        RegisterUserCommand registerCommand = registerMapper.toRegisterCommand(registerRequest);

        registerUserUseCase.register(registerCommand);

        log.info("Registro bem-sucedido");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
