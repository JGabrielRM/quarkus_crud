package org.crud.core.presentation.dto;

public class AuthResponseDTO {

    private String token;
    private Long id;
    private String username;
    private String rol;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, Long id, String username, String rol) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
