package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {

    @Schema(example = "cliente01", description = "O identificador do usuário")
    private String username;
    @Schema(example = "senhaCliente123", description = "A senha em texto plano")
    private String password;

    //Constructors
    public LoginRequest() {}
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    //Getters
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    //Setters
    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
}
