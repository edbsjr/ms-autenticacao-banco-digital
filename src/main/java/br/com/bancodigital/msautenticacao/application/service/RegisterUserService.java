package br.com.bancodigital.msautenticacao.application.service;

import br.com.bancodigital.msautenticacao.application.port.in.RegisterUserUseCase;
import br.com.bancodigital.msautenticacao.application.port.out.UserRepositoryPort;
import br.com.bancodigital.msautenticacao.application.usecase.command.RegisterUserCommand;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import br.com.bancodigital.msautenticacao.domain.model.User;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterUserCommand registerUserCommand) {
        log.info("Iniciando serviço de registro para o usuário: {}", registerUserCommand.login());

        //Valida duplicidade
        if (userRepositoryPort.findByUserName(registerUserCommand.login()).isPresent()) {
            log.warn("Tentativa de cadastro duplicado para login: {}", registerUserCommand.login());
            throw new AuthenticationException(AuthenticationErrorCode.USUARIO_JA_EXISTE);
        }

        // Estabelece padrao para Role nao preenchido
        UserRole roleToSave = (registerUserCommand.role() != null) ? registerUserCommand.role() : UserRole.CLIENTE;

        // Criptografia para a senha
        String encodedPassword = passwordEncoder.encode(registerUserCommand.password());

        // Criação do Objeto de Domínio
        User newUser = new User(
                null, // ID gerado pelo banco
                registerUserCommand.login(),
                encodedPassword,
                roleToSave,
                UserStatus.ATIVO, // Status inicial padrão
                LocalDateTime.now(),
                null // Nunca acessou
        );

        // Persistência
        User savedUser = userRepositoryPort.save(newUser);

        log.info("Usuário salvo com sucesso. ID gerado: {}", savedUser.getId());
        return savedUser;
    }
}
