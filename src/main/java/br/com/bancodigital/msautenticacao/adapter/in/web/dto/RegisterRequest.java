package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import br.com.bancodigital.msautenticacao.domain.model.enums.UserRole;

public record RegisterRequest(
        String username,
        String password,
        UserRole role
) {}
