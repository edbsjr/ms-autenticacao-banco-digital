package br.com.bancodigital.msautenticacao.domain.model;

import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter                 // Gera getters para todos os campos
@AllArgsConstructor     // Gera o construtor com todos os argumentos (necessário para final)
@ToString               // Facilita muito os logs (ver o estado do objeto)
@EqualsAndHashCode      // Importante para comparar objetos corretamente em testes
public class User {

    private final Long id;
    private final String login;
    private final String passwordHash;
    private final UserRole role;      // Ajustei o nome para bater com seu DB e Enum
    private final UserStatus status;  // Ajustei o nome
    private final LocalDateTime createdAt;
    private final LocalDateTime lastAccessDate; // Corrigi o typo 'Acess' -> 'Access'

    // O método de comportamento (regra de negócio) continua aqui manual
    public User recordAccess() {
        return new User(
                this.id,
                this.login,
                this.passwordHash,
                this.role,
                this.status,
                this.createdAt,
                LocalDateTime.now()
        );
    }
}