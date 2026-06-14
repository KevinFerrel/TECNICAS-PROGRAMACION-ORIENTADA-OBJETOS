package modelo;

public class Usuario {
    // Atributos definidos 
    private String username;     // RF39: Nombre de usuario para el inicio de sesión
    private String password;     // RF39: Contraseña de seguridad
    private String rol;          // HU: Define los permisos (Administrador, Recepción, Veterinario, Adopciones)

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario: " + username + " | Perfil: " + rol;
    }
}
