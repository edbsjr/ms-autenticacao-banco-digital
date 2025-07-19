package br.com.bancodigital.msautenticacao.adapter.in.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService (PasswordEncoder passwordEncoder){
        //Usuario em memória cliente
        UserDetails cliente = User.builder()
                .username("cliente01")
                .password(passwordEncoder.encode("senhaCliente123"))
                .roles("CLIENTE")
                .build();

        //Usuário em memória gerente
        UserDetails gerente = User.builder()
                .username("gerente01")
                .password(passwordEncoder.encode("senhaGerente123"))
                .roles("GERENTE")
                .build();

        //Usuário em memória supervisor
        UserDetails supervisor = User.builder()
                .username("supervisor01")
                .password(passwordEncoder.encode("senhaSupervisor123"))
                .roles("SUPERVISOR", "GERENTE")
                .build();

        return new InMemoryUserDetailsManager(cliente, gerente, supervisor);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (Cross-Site Request Forgery) para APIs RESTful, ja possui JWT para segurança
                .csrf(AbstractHttpConfigurer::disable)

                // Configura a política de gerenciamento de sessão como STATELESS.
                // Significa que não há estado de sessão no servidor entre as requisições,
                // crucial para APIs REST com JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público (sem autenticação) a endpoints de autenticação e públicos
                        // Ex: /auth/login, /public/status
                        .requestMatchers("/auth/**", "/public/**").permitAll()

                        // Exige o papel 'CLIENTE' para acessar endpoints de clientes
                        .requestMatchers("/api/clientes/**").hasRole("CLIENTE")

                        // Exige o papel 'GERENTE' OU 'SUPERVISOR' para acessar endpoints de gerência
                        .requestMatchers("/api/gerencia/**").hasAnyRole("GERENTE", "SUPERVISOR")

                        // Exige o papel 'SUPERVISOR' para acessar endpoints de supervisão (mais restritos)
                        .requestMatchers("/api/supervisao/**").hasRole("SUPERVISOR")

                        // Todas as outras requisições exigem que o usuário esteja autenticado (logado),
                        // independentemente do papel.
                        .anyRequest().authenticated()
                );

        // Retorna a cadeia de filtros de segurança configurada
        return http.build();
    }
}
