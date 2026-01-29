package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Schema(example = "cliente01", description = "O identificador do usuário")
        @NotBlank(message = "O login é obrigatório")
        String username,

        @Schema(example = "senhaCliente123", description = "A senha em texto plano")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}

