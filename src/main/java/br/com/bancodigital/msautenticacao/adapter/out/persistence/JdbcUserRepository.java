package br.com.bancodigital.msautenticacao.adapter.out.persistence;

import br.com.bancodigital.msautenticacao.application.port.out.UserRepositoryPort;
import br.com.bancodigital.msautenticacao.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            log.info("Criando novo registro de usuario para o login: {}", user.getLogin());
            return insert(user);
        } else {
            log.debug("Atualizando dados do usuario ID: {}", user.getId());
            return update(user);
        }
    }

    private User insert(User user) {
        String sql = "INSERT INTO users (login, password_hash, role, status, created_at, last_access_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole().name());   // Converte Enum para String
            ps.setString(4, user.getStatus().name()); // Converte Enum para String
            ps.setObject(5, user.getCreatedAt());
            ps.setObject(6, user.getLastAccessDate());
            return ps;
        }, keyHolder);

        // Recupera o ID gerado pelo banco (H2/MySQL)
        Long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        // Retorna uma NOVA instância com o ID preenchido (Imutabilidade)
        return new User(
                newId,
                user.getLogin(),
                user.getPasswordHash(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getLastAccessDate()
        );
    }

    private User update(User user) {
        String sql = "UPDATE users SET login = ?, password_hash = ?, role = ?, status = ?, last_access_date = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getPasswordHash(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getLastAccessDate(),
                user.getId() // WHERE id = ?
        );

        // No update, retornamos o próprio objeto pois o ID não mudou
        return user;
    }

    @Override
    public Optional<User> findByUserName(String login)
    {
        log.debug("Iniciando consulta SQL para o usuario: {}", login);
        String sql = "SELECT id, login, password_hash, role, status, created_at," +
                " last_access_date FROM users WHERE login = ?";

        // ABORDAGEM MODERNA:
        // 1. .query() retorna uma List<User> (vazia ou com elementos), sem lançar erro.
        // 2. .stream().findFirst() pega o primeiro elemento ou retorna Optional vazio.
        return jdbcTemplate.query(sql, new UserRowMapper(), login)
                .stream()
                .findFirst();
    }
}
