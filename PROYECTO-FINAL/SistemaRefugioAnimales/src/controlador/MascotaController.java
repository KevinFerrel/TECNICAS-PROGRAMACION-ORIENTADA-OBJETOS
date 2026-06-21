package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Mascota;

public class MascotaController {
    private List<Mascota> listaMascotas = new ArrayList<>();

    // ======== MÉTODOS PRINCIPALES ========

    public boolean registrarMascota(String idMascota, String nombre, String especie, String raza, LocalDate fechaRescate) {
        if (buscarPorId(idMascota) != null) {
            System.out.println("❌ ID ya registrado: " + idMascota);
            return false;
        }
        Mascota nueva = new Mascota(idMascota, nombre, especie, raza, fechaRescate);
        listaMascotas.add(nueva);
        return true;
    }

    public boolean modificarMascota(String idMascota, String nuevoNombre, String nuevaEspecie, String nuevaRaza) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        m.setNombre(nuevoNombre);
        m.setEspecie(nuevaEspecie);
        m.setRaza(nuevaRaza);
        return true;
    }

    public boolean actualizarEstado(String idMascota, String nuevoEstado) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        String[] validos = {"Disponible", "En cuarentena", "En tratamiento", "Adoptada", "En proceso de adopción"};
        for (String e : validos) {
            if (e.equalsIgnoreCase(nuevoEstado)) {
                m.setEstado(nuevoEstado);
                return true;
            }
        }
        System.out.println("❌ Estado no válido.");
        return false;
    }

    public boolean darDeBajaPorFallecimiento(String idMascota) {
        Mascota m = buscarPorId(idMascota);
        if (m == null) return false;
        m.setActivo(false);
        m.setEstado("Baja");
        return true;
    }

    public boolean ponerEnCuarentena(String idMascota, String motivo) {
        Mascota m = buscarPorId(idMascota);
        if (m == null) return false;
        m.setEstado("En cuarentena");
        System.out.println("✔ Mascota " + idMascota + " en cuarentena. Motivo: " + motivo);
        return true;
    }

    // ======== MÉTODOS DE CONSULTA ========

    public Mascota buscarPorId(String idMascota) {
        for (Mascota m : listaMascotas) {
            if (m.getIdMascota().equalsIgnoreCase(idMascota)) return m;
        }
        return null;
    }

    public List<Mascota> buscarPorNombre(String nombre) {
        List<Mascota> res = new ArrayList<>();
        for (Mascota m : listaMascotas) {
            if (m.getNombre().equalsIgnoreCase(nombre) && m.isActivo()) res.add(m);
        }
        return res;
    }

    public List<Mascota> listarTodas() { return listaMascotas; }

    public List<Mascota> listarDisponibles() {
        List<Mascota> res = new ArrayList<>();
        for (Mascota m : listaMascotas) {
            if (m.isActivo() && m.getEstado().equals("Disponible")) res.add(m);
        }
        return res;
    }

    public List<Mascota> obtenerDisponibles() {
        return listarDisponibles();
    }

    public boolean registrarDescripcionFisica(String idMascota, String color, String tamano, String marcas) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        m.setColor(color);
        m.setTamano(tamano);
        m.setMarcasParticulares(marcas);
        return true;
    }

    public boolean registrarObservacionesConducta(String idMascota, String observaciones) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        m.setObservacionesConducta(observaciones);
        return true;
    }

    // ======== MÉTODOS PARA PERSISTENCIA ========

    public void setListaMascotas(List<Mascota> lista) { this.listaMascotas = lista; }
    public List<Mascota> getListaMascotas() { return listaMascotas; }

    // ======== MÉTODO PARA LISTAR CON ÍNDICE (USADO EN ADOPCIONES) ========

    public void listarDisponiblesConIndice() {
        List<Mascota> disponibles = listarDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay mascotas disponibles.");
        } else {
            System.out.println("\n--- MASCOTAS DISPONIBLES ---");
            System.out.printf("%-3s %-10s %-15s %-10s %-20s%n", "Nº", "ID", "Nombre", "Especie", "Foto");
            for (int i = 0; i < disponibles.size(); i++) {
                Mascota m = disponibles.get(i);
                System.out.printf("%-3d %-10s %-15s %-10s %-20s%n", 
                    (i+1), m.getIdMascota(), m.getNombre(), m.getEspecie(), m.getRutaFoto());
            }
        }
    }
}