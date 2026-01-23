package br.com.bancodigital.msautenticacao.application.service;

import br.com.bancodigital.msautenticacao.application.port.out.UserRepositoryPort;
import br.com.bancodigital.msautenticacao.application.usecase.command.RegisterUserCommand;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import br.com.bancodigital.msautenticacao.domain.model.User;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserService registerUserService;

    private RegisterUserCommand registerUserCommandSucess;
    private RegisterUserCommand registerUserCommandFail;

    @BeforeEach
    void setUp(){
        registerUserCommandSucess = new RegisterUserCommand("ClienteNovo", "SenhaComum", null);
        registerUserCommandFail = new RegisterUserCommand("UsuarioJaExistente", "SenhaComum", null);
    }

    @Test
    @DisplayName("Deve registrar o usuario com sucesso")
    void shouldRegisterUserSuccessfully(){

        // GIVEN
        when(userRepositoryPort.findByUserName(registerUserCommandSucess.login())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(registerUserCommandSucess.password())).thenReturn("hashFalso");

        User userWithId = new User(
                1L,
                "ClienteNovo",
                "hashFalso",
                UserRole.CLIENTE,
                UserStatus.ATIVO,
                LocalDateTime.now(),
                null);

        when(userRepositoryPort.save(any(User.class))).thenReturn(userWithId);

        //WHEN
        User result = registerUserService.register(registerUserCommandSucess);

        //THEN
        assertNotNull(result, "O result não deve ser nulo.");
        assertAll("Validando dados do usuário registrado",
                () -> assertNotNull(result.getId()),
                () -> assertEquals("ClienteNovo", result.getLogin()),
                () -> assertEquals(UserRole.CLIENTE, result.getRole()),
                () -> assertEquals("hashFalso", result.getPasswordHash())
        );

        //VERIFY
        verify(userRepositoryPort, times(1)).findByUserName(registerUserCommandSucess.login());
        verify(passwordEncoder, times(1)).encode(registerUserCommandSucess.password());
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar AutenticacaoException para usuario ja existente")
    void shouldThrowAuthenticationExceptionForExistedUser(){

        //GIVEN
        User existingUser = new User(
                1L,
                "jaexiste",
                "hash",
                UserRole.CLIENTE,
                UserStatus.ATIVO,
                null,
                null);

        when(userRepositoryPort.findByUserName(registerUserCommandFail.login())).
                thenReturn(Optional.of(existingUser));

        //WHEN
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
           registerUserService.register(registerUserCommandFail);
        }, "Deve lançar uma AuthenticationException");

        //THEN
        assertEquals(AuthenticationErrorCode.USUARIO_JA_EXISTE, exception.getErrorCode(),
                "O codigo de erro deve ser USUARIO_JA_EXISTE");

        //VERIFY
        verify(userRepositoryPort, times(1)).findByUserName(registerUserCommandFail.login());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepositoryPort, never()).save(any());
    }
}
