package br.com.bancodigital.msautenticacao.application.service;

import br.com.bancodigital.msautenticacao.adapter.in.web.security.JwtService;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.application.service.AuthService;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;  // Para asserções como assertEquals, assertNotNull, assertThrows
import static org.mockito.Mockito.*; // Para mocks como when, verif

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    LoginCommand loginCommandSuccess;
    LoginCommand loginCommandFail;

    @BeforeEach
    void setUp(){
        loginCommandSuccess = new LoginCommand("cliente01", "senhaCliente123");
        loginCommandFail = new LoginCommand("aaaa", "bbbb");
    }

    @Test
    @DisplayName("Deve autenticar o usuário e gerar o token com sucesso")
    void shouldAuthenticateUserAndGenerateTokenSucessfuly(){
        // Mocks para simular um usuário autenticado e a geração de token
        // Mock do UserDetails (representando o usuário autenticado)
        UserDetails userDetails = new User(loginCommandSuccess.username(),
                "encodedPassword", // A senha aqui não importa muito pois será validada pelo mock do AuthenticationManager
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE")));

        // Mock do Authentication (o objeto que AuthenticationManager retorna em sucesso)
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(userDetails); // Diz que o principal é o userDetails

        // Configura o mock do AuthenticationManager para retornar o mock de Authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        // Configura o mock do JwtService para retornar um token de exemplo
        String expectedToken = "mocked_jwt_token_example";
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        // Ação: Chama o método que queremos testar
        AuthenticatedUser result = authService.authenticate(loginCommandSuccess);

        // Verificação: Asserts para verificar o resultado e comportamento
        assertNotNull(result, "O resultado não deve ser nulo.");
        assertEquals(loginCommandSuccess.username(), result.username(), "O username deve ser o mesmo do comando.");
        assertEquals(expectedToken, result.token(), "O token retornado deve ser o token esperado.");
        assertEquals("ROLE_CLIENTE", result.role(), "O role retornado deve ser 'ROLE_CLIENTE'.");

        // Verifica se os métodos dos mocks foram chamados
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(userDetails);
        verifyNoMoreInteractions(authenticationManager, jwtService); // Garante que nenhum outro método foi chamado nos mocks
    }

    @Test
    @DisplayName("Deve lançar AutenticacaoException para credenciais inválidas")
    void shouldThrowAutenticacaoExceptionForInvalidCredentials() {
        // 1. Cenário: Configura o mock do AuthenticationManager para lançar BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas de teste"));

        // 2. Ação e Verificação: Usa assertThrows para capturar a exceção esperada
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.authenticate(loginCommandFail);
        }, "Deve lançar uma AuthenticationException");

        assertEquals(AuthenticationErrorCode.USUARIO_OU_SENHA_INVALIDOS, exception.getErrorCode(), "O código de erro deve ser USUARIO_OU_SENHA_INVALIDOS.");

        // Verifica se o JwtService não foi chamado
        verify(jwtService, never()).generateToken(any(UserDetails.class));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager, jwtService);
    }
}
