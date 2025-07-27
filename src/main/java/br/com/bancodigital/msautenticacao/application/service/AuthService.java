package br.com.bancodigital.msautenticacao.application.service;

import br.com.bancodigital.msautenticacao.adapter.in.web.security.JwtService;
import br.com.bancodigital.msautenticacao.application.port.in.AuthenticateUserPort;
import br.com.bancodigital.msautenticacao.application.usecase.command.LoginCommand;
import br.com.bancodigital.msautenticacao.domain.exception.AuthenticationException;
import br.com.bancodigital.msautenticacao.domain.exception.errorcode.AuthenticationErrorCode;
import br.com.bancodigital.msautenticacao.domain.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService implements AuthenticateUserPort {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService (AuthenticationManager authenticationManager, JwtService jwtServiceService){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtServiceService;
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        try {
            log.info("Iniciando o processo de autenticacao do usuario {}", loginCommand.username());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCommand.username(), loginCommand.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Extrai o primeiro role (se houver)
            String primaryRole = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            String token = jwtService.generateToken(userDetails);
            log.info("Token JWT gerado para o usuário: {}", userDetails.getUsername());

            // Retorna o modelo de domínio AuthenticatedUser
            if(log.isDebugEnabled()) {
                String safeAuthenticationPayload =
                        String.format("{ \"username\": \"%s\", \"token\": \"[REDACTED]\", \"primaryRole\"}: \"%s\")",
                                userDetails.getUsername(), primaryRole);
                log.debug("Payload da autenticacao do usuário {}",safeAuthenticationPayload);
            }
            return new AuthenticatedUser(userDetails.getUsername(), token, primaryRole);
        } catch (BadCredentialsException e){
            log.error("Error ao tentar autenticar o usuário ou a senha");
            if (log.isDebugEnabled()) {
                log.debug("Detalhes da exceção de autenticação: {}", e.getMessage(), e);
            }
            throw new AuthenticationException (AuthenticationErrorCode.USUARIO_OU_SENHA_INVALIDOS);
        }
    }
}
