package br.com.bancodigital.msautenticacao.adapter.in.web.dto;

public class LoginResponse {

    private String token;
    private String role;

    //Constructos
    public LoginResponse(){};
    public LoginResponse(String token, String role){
        this.token = token;
        this.role = role;
    }

    //Getters
    public String getToken(){return token;}
    public String getRole(){return role;}

    //Setters
    public void setToken(String token){this.token = token;}
    public void setRole(String role){this.role = role;}

}
