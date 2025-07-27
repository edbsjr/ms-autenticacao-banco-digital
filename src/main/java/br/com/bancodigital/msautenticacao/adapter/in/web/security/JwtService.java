package br.com.bancodigital.msautenticacao.adapter.in.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        log.debug("Gerando token JWT sem claims extras para o usuário: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Iniciando a geração de token JWT para o usuário: {}", userDetails.getUsername());

        if (!extraClaims.isEmpty() && log.isDebugEnabled()) {
            log.debug("Claims extras para o token do usuário {}: {}", userDetails.getUsername(), extraClaims);
        }

        return Jwts
                .builder() //Inicia a construcao do JWT
                .setClaims(extraClaims)  // Adiciona claims personalizados
                .setSubject(userDetails.getUsername()) //Define assunto normalmente o usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) //Define o momento de criacao do token
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Define a expiracao do token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina o token com sua chave secreta e algoritmo HS256
                .compact(); // Finaliza a construção e gera a string JWT compacta
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (log.isTraceEnabled()) {
            log.trace("Chave de assinatura JWT carregada e decodificada.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}