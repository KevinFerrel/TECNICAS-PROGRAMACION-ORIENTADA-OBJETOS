package persistencia;

import java.util.List;
import modelo.Mascota;
import modelo.Adoptante;
import modelo.Adopcion;

public class ExcelManager {
    public ExcelManager() {
        System.out.println("ExcelManager iniciado en modo temporal.");
    }

    public void respaldarMascotas(List<Mascota> mascotas) {
        System.out.println("Respaldo de mascotas pendiente de implementación.");
        System.out.println("Para activar esta función se necesita agregar Apache POI al Build Path.");
    }

    public void respaldarAdoptantes(List<Adoptante> adoptantes) {
        System.out.println("Respaldo de adoptantes pendiente de implementación.");
        System.out.println("Para activar esta función se necesita agregar Apache POI al Build Path.");
    }

    public void respaldarDevoluciones(List<Adopcion> expedientes) {
        System.out.println("Respaldo de devoluciones pendiente de implementación.");
        System.out.println("Para activar esta función se necesita agregar Apache POI al Build Path.");
    }
}