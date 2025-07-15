/*package br.com.bancodigital.msautenticacao.adapter.in.web.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Global Exception Handler - Testes de Error")
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND para USUARIO_NAO_ENCONTRADO")
    void shouldReturn404ForUsuarioNaoEncontrado() throws Exception {
        mockMvc.perform(get("/teste-erros/disparar-excecao?tipoErro=usuarioNaoEncontrado"))
                .andExpect(status().isNotFound()) // Verifica o status HTTP
                .andExpect(jsonPath("$.code").value("AUTH-009")) // Verifica o código do erro no JSON
                .andExpect(jsonPath("$.message").value("O usuário especificado não foi encontrado.")) // Verifica a mensagem
                .andExpect(jsonPath("$.httpStatus").value(404)); // Verifica o status HTTP no corpo JSON
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST para USUARIO_OU_SENHA_INVALIDOS")
    void shouldReturn400ForUsuarioOuSenhaInvalidos() throws Exception {
        mockMvc.perform(get("/teste-erros/disparar-excecao?tipoErro=senhaInvalida"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AUTH-001"))
                .andExpect(jsonPath("$.message").value("Usuário ou senha inválidos. Verifique suas credenciais."))
                .andExpect(jsonPath("$.httpStatus").value(400));
    }

    @Test
    @DisplayName("Deve retornar 401 UNAUTHORIZED para TOKEN_EXPIRADO")
    void shouldReturn401ForTokenExpirado() throws Exception {
        mockMvc.perform(get("/teste-erros/disparar-excecao?tipoErro=tokenExpirado"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH-007"))
                .andExpect(jsonPath("$.message").value("Token de autenticação expirado."))
                .andExpect(jsonPath("$.httpStatus").value(401));
    }

    // --- Cenário 2: Testando Exceções Não Mapeadas (Fallback) ---

    @Test
    @DisplayName("Deve retornar 500 INTERNAL SERVER ERROR para exceções não mapeadas")
    void shouldReturn500ForUnhandledExceptions() throws Exception {
        // Lança uma RuntimeException genérica para ver como o Spring (ResponseEntityExceptionHandler) lida
        mockMvc.perform(get("/teste-erros/disparar-excecao?tipoErro=erroGenerico"))
                .andExpect(status().isInternalServerError())
                // O corpo do erro 500 padrão do Spring pode variar, então verificar a mensagem exata pode ser difícil.
                // Mas o status 500 é o mais importante para este teste.
                // Se você adicionar um @ExceptionHandler(Exception.class) no seu GlobalExceptionHandler,
                // você pode personalizar essa resposta e testar o conteúdo do JSON também.
                .andExpect(jsonPath("$.httpStatus").value(500)); // O status no corpo padrão do Spring é 'status'
    }

}
*/