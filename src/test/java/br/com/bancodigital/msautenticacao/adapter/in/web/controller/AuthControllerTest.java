package br.com.bancodigital.msautenticacao.adapter.in.web.controller;

import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginRequest;
import br.com.bancodigital.msautenticacao.adapter.in.web.dto.LoginResponse;
import br.com.bancodigital.msautenticacao.adapter.in.web.mapper.LoginMapper;
import br.com.bancodigital.msautenticacao.application.port.in.AuthenticateUserPort;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;  // Para asserções como assertEquals, assertNotNull, assertThrows
import static org.mockito.Mockito.*; // Para mocks como when, verif

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private LoginMapper loginMapper; //ISSO DEVE SAIR?

    @Mock
    private AuthenticateUserPort authenticateUserPort;

    @InjectMocks
    private AuthController authController;

    LoginRequest loginRequest;
   // LoginResponse loginResponseSucess; //SAI FORA
    //LoginCommand loginCommand;

    @BeforeEach
    void setUp(){
        loginRequest = new LoginRequest("cliente01","senhaCliente01");
        //loginResponseSucess = new LoginResponse("token_valid","ROLE_CLIENTE");//Esse acho que nao deve ser aqui, supostamente ele vai ser gerado mais a frente
    }

    @Test
    @DisplayName("Deve mapear e autenticar o cliente para enviar o Response")
    void shouldMapperAndAuthenticatedUserToSendResponse(){

        LoginCommand loginCommand = new LoginCommand("cliente01", "senhaCliente01");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("cliente01", "token_valid", "ROLE_CLIENTE");
        LoginResponse loginResponse = new LoginResponse("token_valid", "ROLE_CLIENTE");

        //Configura os Mocks que serão usados
        when(loginMapper.toLoginCommand(loginRequest)).thenReturn(loginCommand);
        when(authenticateUserPort.authenticate(loginCommand)).thenReturn(authenticatedUser);
        when(loginMapper.toLoginResponse(authenticatedUser)).thenReturn(loginResponse);

        //Chama o metodo para ser testado
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        //Verificação dos resultados e comportamento
        assertNotNull(response,"O response não deve ser nulo");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());

        //Verificação de comportamento
        verify(loginMapper, times(1)).toLoginCommand(loginRequest);
        verify(authenticateUserPort, times(1)).authenticate(loginCommand);
        verify(loginMapper, times(1)).toLoginResponse(authenticatedUser);

    }

}
