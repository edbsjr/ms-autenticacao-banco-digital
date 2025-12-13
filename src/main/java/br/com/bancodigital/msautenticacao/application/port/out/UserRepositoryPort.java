package br.com.bancodigital.msautenticacao.application.port.out;

import br.com.bancodigital.msautenticacao.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUserName(String login);
    User save(User user);
}
