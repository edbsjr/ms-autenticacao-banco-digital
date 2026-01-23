package br.com.bancodigital.msautenticacao.adapter.out.persistence;

import br.com.bancodigital.msautenticacao.domain.model.User;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import br.com.bancodigital.msautenticacao.domain.model.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(JdbcUserRepository.class)
public class JdbcUserRepositoryTest {

    @Autowired
    private JdbcUserRepository repository;

    @Test
    @DisplayName("Deve salvar novo usuário e retornar ID gerado")
    void shouldSaveNewUserAndReturnID(){
        //GIVEN
        User newUser = new User(null,
                "NovoUsuario",
                "hashPassword",
                UserRole.CLIENTE,
                UserStatus.ATIVO,
                LocalDateTime.now(),
                null);

        //WHEN
        User savedUser = repository.save(newUser);

        //THEN
        assertNotNull(savedUser.getId(), "O ID não deveria ser nulo após salvar");
        assertEquals("NovoUsuario", savedUser.getLogin());
        assertEquals(UserRole.CLIENTE, savedUser.getRole());
        assertEquals(UserStatus.ATIVO, savedUser.getStatus());
    }

    @Test
    @DisplayName("Deve encontrar usuário ja existente no banco")
    void shouldFindExistingUser(){
        //GIVEN(Ja foi salvo "cliente01")

        //WHEN
        Optional<User> userOptional = repository.findByUserName("cliente01");

        //THEN
        assertTrue(userOptional.isPresent(), "Deveria encontrar cliente01");

        User user = userOptional.get();
        assertAll("Validando dados do usuário encontrado",
                () -> assertNotNull(user.getId()),
                () -> assertEquals("cliente01", user.getLogin()),
                () -> assertEquals(UserRole.CLIENTE, user.getRole()),
                () -> assertEquals(UserStatus.ATIVO, user.getStatus())
        );
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar usuário inexistente")
    void shouldReturnEmptyForNonExistentUser() {
        // WHEN
        Optional<User> result = repository.findByUserName("fantasma");

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar usuário já existente")
    void shouldUpdateExistingUser(){
        // GIVEN - Pegamos o cliente01
        User oldUser = repository.findByUserName("cliente01").get();

        User userToUpdate = new User(
                oldUser.getId(),
                oldUser.getLogin(),
                oldUser.getPasswordHash(),
                oldUser.getRole(),
                UserStatus.BLOQUEADO, // <--- Mudamos aqui
                oldUser.getCreatedAt(),
                oldUser.getLastAccessDate());

        // WHEN
        repository.save(userToUpdate);

        // THEN
        User userFromDb = repository.findByUserName("cliente01").get();

        assertAll("Validando que a atualização persistiu no banco",
                () -> assertEquals(oldUser.getId(), userFromDb.getId()),
                () -> assertEquals(UserStatus.BLOQUEADO, userFromDb.getStatus()),
                () -> assertEquals("cliente01", userFromDb.getLogin())
        );
    }
}
