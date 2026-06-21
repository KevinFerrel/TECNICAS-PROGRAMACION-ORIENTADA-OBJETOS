package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class SeguridadController {
    private List<Usuario> usuariosRegistrados = new ArrayList<>();
    private Usuario usuarioSesionActual = null;

    public SeguridadController() {
        usuariosRegistrados.add(new Usuario("admin", "admin123", "Administrador del Refugio"));
        usuariosRegistrados.add(new Usuario("recepcion", "rec123", "Personal de Recepción"));
        usuariosRegistrados.add(new Usuario("veterinario", "vet123", "Veterinario"));
        usuariosRegistrados.add(new Usuario("adopciones", "adop123", "Encargado de Adopciones"));
    }

    public boolean login(String username, String password) {
        for (Usuario u : usuariosRegistrados) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                usuarioSesionActual = u;
                System.out.println("✔ Acceso autorizado. Bienvenido " + u.getUsername());
                return true;
            }
        }
        System.out.println("❌ Credenciales incorrectas.");
        return false;
    }

    public void logout() {
        if (usuarioSesionActual != null) {
            System.out.println("Cerrando sesión de " + usuarioSesionActual.getUsername());
            usuarioSesionActual = null;
        } else {
            System.out.println("No hay sesión activa.");
        }
    }

    public Usuario getUsuarioSesionActual() { return usuarioSesionActual; }
}