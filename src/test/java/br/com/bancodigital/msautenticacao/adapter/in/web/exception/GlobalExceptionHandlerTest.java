package br.com.bancodigital.msautenticacao.adapter.in.web.exception;

import br.com.bancodigital.msautenticacao.adapter.in.web.exception.handler.GlobalExceptionHandler;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        // Configura o ambiente isolado com o Controller Dublê e o seu Handler
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    // --- CONTROLLER DUBLÊ (Apenas para lançar os erros) ---
    @RestController
    static class TestController {
        @GetMapping("/teste/usuario-nao-encontrado")
        public void throwUsuarioNaoEncontrado() {
            throw new AuthenticationException(AuthenticationErrorCode.USUARIO_NAO_ENCONTRADO);
        }

        @GetMapping("/teste/credenciais-invalidas")
        public void throwCredenciaisInvalidas() {
            throw new AuthenticationException(AuthenticationErrorCode.USUARIO_OU_SENHA_INVALIDOS);
        }

        @GetMapping("/teste/token-expirado")
        public void throwTokenExpirado() {
            // Supondo que você tenha esse erro mapeado no Enum ou uma Exception específica
            throw new AuthenticationException(AuthenticationErrorCode.TOKEN_EXPIRADO);
        }

        @GetMapping("/teste/erro-generico")
        public void throwGenericException() {
            throw new RuntimeException("Erro inesperado simulado");
        }
    }

    // --- SEUS TESTES (Adaptados para chamar as rotas do Dublê) ---

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND para USUARIO_NAO_ENCONTRADO")
    void shouldReturn404ForUsuarioNaoEncontrado() throws Exception {
        mockMvc.perform(get("/teste/usuario-nao-encontrado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("AUTH-009")) // Confirme se o código no Enum é esse mesmo
                .andExpect(jsonPath("$.httpStatus").value(404));
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST para USUARIO_OU_SENHA_INVALIDOS")
    void shouldReturn400ForUsuarioOuSenhaInvalidos() throws Exception {
        mockMvc.perform(get("/teste/credenciais-invalidas"))
                .andExpect(status().isBadRequest()) // Ou isUnprocessableEntity() dependendo da sua config
                .andExpect(jsonPath("$.code").value("AUTH-001"))
                .andExpect(jsonPath("$.httpStatus").value(400));
    }

    @Test
    @DisplayName("Deve retornar 401 UNAUTHORIZED para TOKEN_EXPIRADO")
    void shouldReturn401ForTokenExpirado() throws Exception {
        mockMvc.perform(get("/teste/token-expirado"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH-007"))
                .andExpect(jsonPath("$.httpStatus").value(401));
    }

    @Test
    @DisplayName("Deve retornar 500 INTERNAL SERVER ERROR para exceções genéricas")
    void shouldReturn500ForGenericException() throws Exception {
        mockMvc.perform(get("/teste/erro-generico"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.httpStatus").value(500));
    }
}