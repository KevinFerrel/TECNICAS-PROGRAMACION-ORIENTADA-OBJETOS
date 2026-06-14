package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Mascota;

public class MascotaController {
    // Colección en memoria que actúa como repositorio temporal antes de enviarse a Excel
    private List<Mascota> listaMascotas = new ArrayList<>();

    // HU01 / RF01: Registrar una nueva mascota validando unicidad de ID
    public boolean registrarMascota(String idMascota, String nombre, String especie, String raza, LocalDate fechaRescate) {
        if (buscarPorId(idMascota) != null) {
            System.out.println("Error: El código identificador '" + idMascota + "' ya se encuentra registrado.");
            return false;
        }
        Mascota nuevaMascota = new Mascota(idMascota, nombre, especie, raza, fechaRescate);
        listaMascotas.add(nuevaMascota);
        return true;
    }

    // RF02: Modificar los datos descriptivos básicos de una mascota existente
    public boolean modificarMascota(String idMascota, String nuevoNombre, String nuevaEspecie, String nuevaRaza) {
        Mascota mascota = buscarPorId(idMascota);
        if (mascota == null || !mascota.isActivo()) return false;
        
        mascota.setNombre(nuevoNombre);
        mascota.setEspecie(nuevaEspecie);
        mascota.setRaza(nuevaRaza);
        return true;
    }

    // HU02 / RF03 & UML: actualizarEstado() cambia el flujo operativo del animal
    public boolean actualizarEstado(String idMascota, String nuevoEstado) {
        Mascota mascota = buscarPorId(idMascota);
        if (mascota == null || !mascota.isActivo()) return false;

        if (nuevoEstado.equals("Disponible") || nuevoEstado.equals("En cuarentena") || 
            nuevoEstado.equals("En tratamiento") || nuevoEstado.equals("Adoptada") ||
            nuevoEstado.equals("En proceso de adopción")) {
            mascota.setEstado(nuevoEstado);
            return true;
        }
        System.out.println("Error: Estado operativo no válido para el sistema.");
        return false;
    }

    // RF04: Ejecutar la baja lógica de la mascota en caso de fallecimiento
    public boolean darDeBajaPorFallecimiento(String idMascota) {
        Mascota mascota = buscarPorId(idMascota);
        if (mascota == null) return false;
        
        mascota.setActivo(false);
        mascota.setEstado("Baja");
        return true;
    }

    // RF05 & UML: Buscar una mascota por su ID único
    public Mascota buscarPorId(String idMascota) {
        for (Mascota m : listaMascotas) {
            if (m.getIdMascota().equalsIgnoreCase(idMascota)) {
                return m;
            }
        }
        return null;
    }

    // RF05: Buscar coincidencias de mascotas por su nombre
    public List<Mascota> buscarPorNombre(String nombre) {
        List<Mascota> resultados = new ArrayList<>();
        for (Mascota m : listaMascotas) {
            if (m.getNombre().equalsIgnoreCase(nombre) && m.isActivo()) {
                resultados.add(m);
            }
        }
        return resultados;
    }

    // HU02 / RF06: Retornar el listado completo de mascotas registradas
    public List<Mascota> listarTodas() {
        return listaMascotas;
    }

    // RF07: Filtrar y listar únicamente los animales con estado "Disponible"
    public List<Mascota> listarDisponibles() {
        List<Mascota> disponibles = new ArrayList<>();
        for (Mascota m : listaMascotas) {
            if (m.isActivo() && m.getEstado().equals("Disponible")) {
                disponibles.add(m);
            }
        }
        return disponibles;
    }

    // RF08: Guardar las especificaciones y rasgos físicos detallados
    public boolean registrarDescripcionFisica(String idMascota, String color, String tamano, String marcas) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        
        m.setColor(color);
        m.setTamano(tamano);
        m.setMarcasParticulares(marcas);
        return true;
    }

    // RF09: Anotar observaciones conductuales críticas detectadas
    public boolean registrarObservacionesConducta(String idMascota, String observaciones) {
        Mascota m = buscarPorId(idMascota);
        if (m == null || !m.isActivo()) return false;
        
        m.setObservacionesConducta(observaciones);
        return true;
    }
}
