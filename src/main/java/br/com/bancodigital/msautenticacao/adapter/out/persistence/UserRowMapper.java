package br.com.bancodigital.msautenticacao.adapter.out.persistence;

import br.com.bancodigital.msautenticacao.domain.model.User;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        // 1. Tratamento de Datas (pode ser nulo no banco) - Evita erro quando vier nulo do banco
        Timestamp lastAccessTimestamp = rs.getTimestamp("last_access_date");
        var lastAccessDate = (lastAccessTimestamp != null) ? lastAccessTimestamp.toLocalDateTime() : null;

        // 2. Tratamento de Timestamp
        var createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        // 3. Conversão de String para Enum
        UserRole role = UserRole.valueOf(rs.getString("role"));
        UserStatus status = UserStatus.valueOf(rs.getString("status"));

        // 4. Construção do Objeto Imutável
        return new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("password_hash"),
                role,
                status,
                createdAt,
                lastAccessDate
        );
    }
}
