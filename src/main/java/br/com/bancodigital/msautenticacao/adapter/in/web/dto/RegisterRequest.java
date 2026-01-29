package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Schema(example = "gerenteNovo", description = "O identificador do novo usuário")
        @NotBlank(message = "O login é obrigatório")
        String username,

        @Schema(example = "senhaNova123", description = "A senha em texto plano")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @Schema(example = "GERENTE", description = "O perfil do novo usuário")
        @NotNull(message = "O perfil é obrigatório")
        UserRole role
) {}
