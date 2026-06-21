package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String rol;

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return username + " (" + rol + ")";
    }
}