package br.com.bancodigital.msautenticacao.adapter.in.security;

import br.com.bancodigital.msautenticacao.adapter.out.security.JwtService;
import br.com.bancodigital.msautenticacao.domain.model.User;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private CustomUserDetails customUserDetails;

    // Usamos uma chave válida de 256 bits (HMAC-SHA256)
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250655368566D5971";
    private final long EXPIRATION_TIME = 3600000L; // 1 hora

    @BeforeEach
    void SetUp(){
        // Prepara os dados reais
        User userReal = new User(1L, "UsuarioTeste", "hash", UserRole.CLIENTE, UserStatus.ATIVO, null, null);
        customUserDetails = new CustomUserDetails(userReal);

        // Injeta os valores das variáveis @Value
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION_TIME);
    }

    @Test
    @DisplayName("Deve gerar token contendo ID, Role e Expiração corretos")
    void shouldGenerateTokenWithCorrectClaims() {
        // 1. AÇÃO: Gera o token
        String token = jwtService.generateToken(customUserDetails);

        // 2. VERIFICAÇÃO
        assertNotNull(token);

        // Recria a chave para decodificar
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        // Abre o token para inspecionar
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // 3. ASSERÇÕES
        // Valida Subject (Username)
        assertEquals("UsuarioTeste", claims.getSubject());

        // Valida Claim Extra: ID
        assertEquals(1L, ((Number) claims.get("id")).longValue());

        // Valida Claim Extra: Role
        assertEquals("ROLE_CLIENTE", claims.get("role"));

        // Valida Expiração
        Date expiration = claims.getExpiration();
        assertNotNull(expiration);

        // Verifica se expira no futuro (margem de segurança de alguns segundos)
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o UserDetails for Nulo")
    void shouldThrowExceptionWhenUserDetailsIsNull(){

        assertThrows(NullPointerException.class, () -> {
            jwtService.generateToken(null);
        });
    }
}