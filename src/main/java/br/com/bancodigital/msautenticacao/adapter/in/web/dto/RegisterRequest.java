package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(
        @Schema(example = "gerenteNovo", description = "O identificador do novo usuário")
        String username,
        @Schema(example = "senhaNova123", description = "A senha em texto plano")
        String password,
        @Schema(example = "GERENTE", description = "O perfil do novo usuário")
        UserRole role
) {}
