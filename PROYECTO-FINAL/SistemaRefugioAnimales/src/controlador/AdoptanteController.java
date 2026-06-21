package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Adoptante;

public class AdoptanteController {
    private List<Adoptante> listaAdoptantes = new ArrayList<>();

    public void registrarAdoptante(String dni, String nombre, int edad, String telefono, String email) {
        if (buscarPorDni(dni) != null) {
            System.out.println("⚠️ DNI ya registrado: " + dni);
            return;
        }
        Adoptante nuevo = new Adoptante(dni, nombre, edad, telefono, email);
        listaAdoptantes.add(nuevo);
        System.out.println("✔ Adoptante registrado: " + nombre);
    }

    public boolean actualizarDatosContacto(String dni, String nuevoTelefono, String nuevoEmail) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;
        a.setTelefono(nuevoTelefono);
        a.setEmail(nuevoEmail);
        return true;
    }

    public boolean completarPerfilAdoptante(String dni, String direccion, String preferencias, String tipoVivienda) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;
        a.setDireccion(direccion);
        a.setPreferencias(preferencias);
        a.setTipoVivienda(tipoVivienda);
        return true;
    }

    public boolean evaluarAdoptante(String dni, String nuevoEstado, String motivoComentario) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;
        if (nuevoEstado.equalsIgnoreCase("Rechazado")) {
            if (motivoComentario == null || motivoComentario.trim().isEmpty()) {
                System.out.println("❌ Debe ingresar un motivo de rechazo.");
                return false;
            }
        }
        a.setEstadoValidacion(nuevoEstado, motivoComentario);
        System.out.println("✔ Estado actualizado a '" + nuevoEstado + "' para DNI " + dni);
        return true;
    }

    public Adoptante buscarPorDni(String dni) {
        for (Adoptante a : listaAdoptantes) {
            if (a.getDni().equals(dni)) return a;
        }
        return null;
    }

    public List<Adoptante> listarAdoptantesAprobados() {
        List<Adoptante> res = new ArrayList<>();
        for (Adoptante a : listaAdoptantes) {
            if (a.getEstadoValidacion().equalsIgnoreCase("Aprobado")) res.add(a);
        }
        return res;
    }

    public List<Adoptante> obtenerTodos() { return listaAdoptantes; }

    public void setListaAdoptantes(List<Adoptante> lista) { this.listaAdoptantes = lista; }
    public List<Adoptante> getListaAdoptantes() { return listaAdoptantes; }
}