package controlador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Mascota;

public class GestorArchivos {
    private static final String ARCHIVO_MASCOTAS = "mascotas.dat";

    public static void guardarMascotas(List<Mascota> listaMascotas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_MASCOTAS))) {
            oos.writeObject(listaMascotas);
            System.out.println("✅ Datos guardados en " + ARCHIVO_MASCOTAS);
        } catch (IOException e) {
            System.out.println("❌ Error al guardar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Mascota> cargarMascotas() {
        List<Mascota> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_MASCOTAS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                lista = (List<Mascota>) ois.readObject();
                System.out.println("✅ Datos cargados desde " + ARCHIVO_MASCOTAS);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("❌ Error al cargar: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ No hay datos previos.");
        }
        return lista;
    }
}