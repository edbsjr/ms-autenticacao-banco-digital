package br.com.bancodigital.msautenticacao.adapter.in.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User; // Importação necessária
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    UserDetails userDetails;


    @BeforeEach
    void SetUp(){
        userDetails = new User("UsuarioTeste","SenhaTeste", Collections.emptyList());

        // Injetando valores para os campos anotados com @Value
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250655368566D5971");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hora
    }

    @Test
    @DisplayName("Deve gerar o token JWT e ter o subject e data de expiração corretos")
    void shouldGenerateTokenJwtAndReturnCorrectClaims() {
        //Gera o token usando o JwtService
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        // 2. Verificação 1: O token deve conter o subject correto
        String subject = Jwts.parser()
                .setSigningKey(Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250655368566D5971"))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals("UsuarioTeste", subject, "O subject do token deve ser o username.");

        // 3. Verificação 2: O token deve expirar no tempo correto
        Claims claims = Jwts.parser()
                .setSigningKey(Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250655368566D5971"))
                .build()
                .parseClaimsJws(token)
                .getBody();
        long expirationTime = claims.getExpiration().getTime();
        long issuedAtTime = claims.getIssuedAt().getTime();
        long expectedExpirationTime = issuedAtTime + 3600000L;
        assertEquals(expectedExpirationTime, expirationTime, 100, "A data de expiração deve ser 1 hora após a emissão.");

    }


}
