package br.com.bancodigital.msautenticacao.adapter.out.security;

import br.com.bancodigital.msautenticacao.adapter.in.security.CustomUserDetails;
import br.com.bancodigital.msautenticacao.application.port.out.TokenProviderPort;
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
public class JwtService implements TokenProviderPort {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Override
    public String generateToken(CustomUserDetails userDetails) {
        log.debug("Preparando claims (ID e Role) para o usuário: {}", userDetails.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userDetails.getId());

        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        extraClaims.put("role", role);

        return buildToken(extraClaims, userDetails);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Iniciando a geração de token JWT para o usuário: {}", userDetails.getUsername());

        if (!extraClaims.isEmpty() && log.isDebugEnabled()) {
            log.debug("Claims extras injetados: {}", extraClaims);
        }

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (log.isTraceEnabled()) {
            log.trace("Chave de assinatura JWT carregada.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}