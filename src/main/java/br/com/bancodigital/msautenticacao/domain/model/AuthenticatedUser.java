package br.com.bancodigital.msautenticacao.domain.model;

public record AuthenticatedUser(
        String username,
        String token,
        String role) {}
