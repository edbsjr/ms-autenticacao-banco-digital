package br.com.bancodigital.msautenticacao.application.port.out;

import br.com.bancodigital.msautenticacao.adapter.in.security.CustomUserDetails;

public interface TokenProviderPort {
    // Recebe o CustomUserDetails, que tem tudo (ID, Login, Role)
    String generateToken(CustomUserDetails userDetails);
}