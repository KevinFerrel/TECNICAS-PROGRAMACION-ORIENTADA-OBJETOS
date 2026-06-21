package controlador;

import java.io.*;
import java.util.List;
import modelo.Mascota;
import modelo.Adoptante;
import modelo.Adopcion;
import modelo.CitaMedica;
import modelo.Tratamiento;
import modelo.Vacuna;

public class PersistenciaManager {
    private static final String ARCHIVO_DATOS = "refugio_data.ser";

    public static void guardarDatos(MascotaController mc, AdoptanteController ac, AdopcionController adoc, HistorialMedicoController hmc) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(mc.getListaMascotas());
            oos.writeObject(ac.getListaAdoptantes());
            oos.writeObject(adoc.getListaAdopciones());
            oos.writeObject(hmc.getListadoCitas());
            oos.writeObject(hmc.getListadoTratamientos());
            oos.writeObject(hmc.getListadoVacunas());
            System.out.println("✅ Datos guardados en " + ARCHIVO_DATOS);
        } catch (IOException e) {
            System.out.println("❌ Error al guardar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarDatos(MascotaController mc, AdoptanteController ac, AdopcionController adoc, HistorialMedicoController hmc) {
        File f = new File(ARCHIVO_DATOS);
        if (!f.exists()) {
            System.out.println("ℹ️ No hay datos previos.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            mc.setListaMascotas((List<Mascota>) ois.readObject());
            ac.setListaAdoptantes((List<Adoptante>) ois.readObject());
            adoc.setListaAdopciones((List<Adopcion>) ois.readObject());
            hmc.setListadoCitas((List<CitaMedica>) ois.readObject());
            hmc.setListadoTratamientos((List<Tratamiento>) ois.readObject());
            hmc.setListadoVacunas((List<Vacuna>) ois.readObject());
            System.out.println("✅ Datos cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error al cargar: " + e.getMessage());
        }
    }
}