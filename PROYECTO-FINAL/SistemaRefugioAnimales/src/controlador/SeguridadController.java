package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class SeguridadController {
    private List<Usuario> usuariosRegistrados = new ArrayList<>();
    private Usuario usuarioSesionActual = null; // Mantiene el rastro del usuario logueado

    public SeguridadController() {
        // Carga inicial de perfiles basada en las Historias de Usuario (HU01 al HU10)
        usuariosRegistrados.add(new Usuario("admin", "admin123", "Administrador del Refugio"));
        usuariosRegistrados.add(new Usuario("recepcion", "rec123", "Personal de Recepción"));
        usuariosRegistrados.add(new Usuario("veterinario", "vet123", "Veterinario"));
        usuariosRegistrados.add(new Usuario("adopciones", "adop123", "Encargado de Adopciones"));
    }

    // RF39 y UML: Validar credenciales y conceder acceso
    public boolean login(String username, String password) {
        for (Usuario u : usuariosRegistrados) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                usuarioSesionActual = u;
                System.out.println("✔ ACCESO AUTORIZADO. Bienvenido/a, " + u.getUsername() + " (" + u.getRol() + ")");
                return true;
            }
        }
        System.out.println("❌ ERROR: Credenciales incorrectas. Acceso denegado.");
        return false;
    }

    // RF40 y UML: Finalizar la sesión de forma segura
    public void logout() {
        if (usuarioSesionActual != null) {
            System.out.println("Cerrando sesión de " + usuarioSesionActual.getUsername() + "...");
            usuarioSesionActual = null;
            System.out.println("✔ Sesión finalizada correctamente.");
        } else {
            System.out.println("No hay ninguna sesión activa en este momento.");
        }
    }

    public Usuario getUsuarioSesionActual() {
        return usuarioSesionActual;
    }
}