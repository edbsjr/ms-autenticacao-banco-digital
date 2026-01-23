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
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;  // Para asserções como assertEquals, assertNotNull, assertThrows
import static org.mockito.Mockito.*; // Para mocks como when, verif

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private LoginMapper loginMapper;

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @InjectMocks
    private AuthController authController;

    LoginRequest loginRequest;
    RegisterRequest registerRequest;

    @BeforeEach
    void setUp(){
        loginRequest = new LoginRequest("cliente01","senhaCliente01");
        //loginResponseSucess = new LoginResponse("token_valid","ROLE_CLIENTE");//Esse acho que nao deve ser aqui, supostamente ele vai ser gerado mais a frente
        registerRequest = new RegisterRequest("cliente02", "senhaCliente02", UserRole.CLIENTE);
    }

    @Test
    @DisplayName("Deve mapear e autenticar o cliente para enviar o Response")
    void shouldMapperAndAuthenticatedUserToSendResponse(){

        LoginCommand loginCommand = new LoginCommand("cliente01", "senhaCliente01");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("cliente01", "token_valid", "ROLE_CLIENTE");
        LoginResponse loginResponse = new LoginResponse("token_valid", "ROLE_CLIENTE");

        //Configura os Mocks que serão usados
        when(loginMapper.toLoginCommand(loginRequest)).thenReturn(loginCommand);
        when(loginUseCase.authenticate(loginCommand)).thenReturn(authenticatedUser);
        when(loginMapper.toLoginResponse(authenticatedUser)).thenReturn(loginResponse);

        //Chama o metodo para ser testado
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        //Verificação dos resultados e comportamento
        assertNotNull(response,"O response não deve ser nulo");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());

        //Verificação de comportamento
        verify(loginMapper, times(1)).toLoginCommand(loginRequest);
        verify(loginUseCase, times(1)).authenticate(loginCommand);
        verify(loginMapper, times(1)).toLoginResponse(authenticatedUser);

    }

    @Test
    @DisplayName("Deve registrar usuário e retornar Created")
    void shouldRegisterUserAndReturnCreated(){
        RegisterUserCommand registerUserCommand = new RegisterUserCommand("cliente02", "senhaCliente02", UserRole.CLIENTE);

        when(registerMapper.toRegisterCommand(registerRequest)).thenReturn(registerUserCommand);
        when(registerUserUseCase.register(registerUserCommand)).thenReturn(null);

        ResponseEntity<Void> response = authController.register(registerRequest);

        assertNotNull(response, "A resposta não deveria ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(registerMapper, times(1)).toRegisterCommand((registerRequest));
        verify(registerUserUseCase, times(1)).register(registerUserCommand);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar registrar usuário duplicado")
    void shouldThrowExceptionWhenRegisteringDuplicateUser() {
        // GIVEN
        RegisterUserCommand registerUserCommand = new RegisterUserCommand("duplicado", "123", UserRole.CLIENTE);

        when(registerMapper.toRegisterCommand(registerRequest)).thenReturn(registerUserCommand);

        // MOCK DO ERRO
        doThrow(new AuthenticationException(AuthenticationErrorCode.USUARIO_JA_EXISTE))
                .when(registerUserUseCase).register(registerUserCommand);

        // WHEN & THEN
        assertThrows(AuthenticationException.class, () -> {
            authController.register(registerRequest);
        }, "Deveria ter lançado uma exceção de autenticação");

        verify(registerUserUseCase, times(1)).register(registerUserCommand);
    }

    @Test
    @DisplayName("Deve lançar exceção quando der senha/login invalidos")
    void shouldThrowExceptionWhenPasswordOrLoginIsWrong(){

        LoginRequest wrongRequest = new LoginRequest("usuarioErrado", "senhaErrada");
        LoginCommand wrongCommand = new LoginCommand("usuarioErrado", "senhaErrada");


        when(loginMapper.toLoginCommand(wrongRequest)).thenReturn(wrongCommand);

        doThrow(new AuthenticationException(AuthenticationErrorCode.USUARIO_OU_SENHA_INVALIDOS))
                .when(loginUseCase).authenticate(wrongCommand);

        assertThrows(AuthenticationException.class, () ->{
            authController.login(wrongRequest);
        }, "Deveria ter lançado exceção de credenciais inválidas");

        verify(loginUseCase, times(1)).authenticate(wrongCommand);
    }

}
