package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Adoptante;

public class AdoptanteController {
    // Repositorio temporal en memoria para los datos de los adoptantes
    private List<Adoptante> listaAdoptantes = new ArrayList<>();

    // RF23 y RF24: Registrar validando explícitamente que el DNI no exista previamente
    public void registrarAdoptante(String dni, String nombre, int edad, String telefono) {
        // Bloqueo de duplicidad y mensaje exigido por las restricciones
        if (buscarPorDni(dni) != null) {
            System.out.println("⚠️ USUARIO YA REGISTRADO (DNI: " + dni + "). No se puede duplicar el registro.");
            return;
        }
        Adoptante nuevo = new Adoptante(dni, nombre, edad, telefono);
        listaAdoptantes.add(nuevo);
        System.out.println("✔ Registro exitoso: Adoptante " + nombre + " ingresado al sistema.");
    }

    // RF25: Actualizar datos de contacto y domicilio
    public boolean actualizarDatosContacto(String dni, String nuevoTelefono, String nuevaDireccion) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;

        a.setTelefono(nuevoTelefono);
        a.setDireccion(nuevaDireccion);
        return true;
    }

    // RF26 y RF27: Completar el perfil socioeconómico y preferencias
    public boolean completarPerfilAdoptante(String dni, String preferencias, String tipoVivienda) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;

        a.setPreferencias(preferencias);
        a.setTipoVivienda(tipoVivienda);
        return true;
    }

    // RF28 y RF29: Cambiar el estado de validación garantizando el cuadro de comentarios si es Rechazado
    public boolean evaluarAdoptante(String dni, String nuevoEstado, String motivoComentario) {
        Adoptante a = buscarPorDni(dni);
        if (a == null) return false;

        // Regla de negocio estricta: El rechazo exige justificación obligatoria
        if (nuevoEstado.equalsIgnoreCase("Rechazado")) {
            if (motivoComentario == null || motivoComentario.trim().isEmpty()) {
                System.out.println("Error Crítico: Debe llenar el cuadro de comentarios indicando el motivo del rechazo.");
                return false;
            }
        }
        
        // Se aplica el cambio de estado junto con el posible comentario
        a.setEstadoValidacion(nuevoEstado, motivoComentario);
        System.out.println("✔ Estado actualizado a '" + nuevoEstado + "' para el DNI: " + dni);
        return true;
    }

    // RF30: Búsqueda de perfil por DNI
    public Adoptante buscarPorDni(String dni) {
        for (Adoptante a : listaAdoptantes) {
            if (a.getDni().equals(dni)) {
                return a;
            }
        }
        return null;
    }

    // RF31: Retornar exclusivamente la lista de adoptantes aptos para proceder
    public List<Adoptante> listarAdoptantesAprobados() {
        List<Adoptante> aprobados = new ArrayList<>();
        for (Adoptante a : listaAdoptantes) {
            if (a.getEstadoValidacion().equalsIgnoreCase("Aprobado")) {
                aprobados.add(a);
            }
        }
        return aprobados;
    }

    // Método para extraer todos los registros (necesario para el módulo de Excel)
    public List<Adoptante> obtenerTodos() {
        return listaAdoptantes;
    }
}