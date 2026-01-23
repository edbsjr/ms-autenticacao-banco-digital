package br.com.bancodigital.msautenticacao.adapter.in.security;

import br.com.bancodigital.msautenticacao.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("Tentativa de carregamento de detalhes do usuario: {}", login);


        // 2. Chama a Porta de Saída
        return userRepositoryPort.findByUserName(login)
                // 3. Mapeia o Optional para a nossa classe de adaptação
                .map(CustomUserDetails::new)
                // 4. Se o Optional estiver vazio, lança a exceção exigida pelo Spring Security
                .orElseThrow(() -> {
                    log.warn("Falha ao encontrar o usuario: {}", login);
                    // Exceção de Negócio (Autenticação) lançada na Camada de Serviço/Adaptador
                    return new UsernameNotFoundException("Usuário não encontrado: " + login);
                });
    }
}
