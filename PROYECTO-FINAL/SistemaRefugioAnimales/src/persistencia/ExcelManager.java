package persistencia;
//import java.io.File;
//import java.io.FileOutputStream;
import java.util.List;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import modelo.Mascota;
import modelo.Adoptante;
import modelo.Adopcion;

public class ExcelManager {

    public ExcelManager() {
        // Constructor temporal.
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
/*public class ExcelManager {
	
    // Carpeta local que debe existir o crearse en la raíz del proyecto
    private static final String RUTA_CARPETA = "datos";

    public ExcelManager() {
        File directorio = new File(RUTA_CARPETA);
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea la carpeta automáticamente si no existe
        }
    }

    // Método para respaldar el inventario de Mascotas
    public void respaldarMascotas(List<Mascota> mascotas) {
        Workbook libro = new XSSFWorkbook();
        Sheet hoja = libro.createSheet("Inventario_Mascotas");
        
        Row cabecera = hoja.createRow(0);
        String[] titulos = {"ID Mascota", "Nombre", "Especie", "Raza", "Estado", "Conducta"};
        for (int i = 0; i < titulos.length; i++) cabecera.createCell(i).setCellValue(titulos[i]);

        int filaActual = 1;
        for (Mascota m : mascotas) {
            Row fila = hoja.createRow(filaActual++);
            fila.createCell(0).setCellValue(m.getIdMascota());
            fila.createCell(1).setCellValue(m.getNombre());
            fila.createCell(2).setCellValue(m.getEspecie());
            fila.createCell(3).setCellValue(m.getRaza() != null ? m.getRaza() : "N/A");
            fila.createCell(4).setCellValue(m.getEstado());
            fila.createCell(5).setCellValue(m.getObservacionesConducta() != null ? m.getObservacionesConducta() : "Sin observaciones");
        }
        guardarArchivo(libro, "Mascotas.xlsx");
    }

    // Método para respaldar el listado de Adoptantes y sus motivos de rechazo
    public void respaldarAdoptantes(List<Adoptante> adoptantes) {
        Workbook libro = new XSSFWorkbook();
        Sheet hoja = libro.createSheet("Adoptantes_Registrados");
        
        Row cabecera = hoja.createRow(0);
        String[] titulos = {"DNI", "Nombre", "Teléfono", "Estado", "Motivo de Rechazo"};
        for (int i = 0; i < titulos.length; i++) cabecera.createCell(i).setCellValue(titulos[i]);

        int filaActual = 1;
        for (Adoptante a : adoptantes) {
            Row fila = hoja.createRow(filaActual++);
            fila.createCell(0).setCellValue(a.getDni());
            fila.createCell(1).setCellValue(a.getNombre());
            fila.createCell(2).setCellValue(a.getTelefono());
            fila.createCell(3).setCellValue(a.getEstadoValidacion());
            fila.createCell(4).setCellValue(a.getMotivoRechazo() != null ? a.getMotivoRechazo() : "");
        }
        guardarArchivo(libro, "Adoptantes.xlsx");
    }

    // Método exigido para llevar el registro aislado de animales devueltos
    public void respaldarDevoluciones(List<Adopcion> expedientes) {
        Workbook libro = new XSSFWorkbook();
        Sheet hoja = libro.createSheet("Historial_Devoluciones");
        
        Row cabecera = hoja.createRow(0);
        String[] titulos = {"ID Adopcion", "ID Mascota", "DNI Adoptante", "Causa de Devolución", "Detalles Extras"};
        for (int i = 0; i < titulos.length; i++) cabecera.createCell(i).setCellValue(titulos[i]);

        int filaActual = 1;
        for (Adopcion a : expedientes) {
            if (a.isEsDevuelta()) { // Solo guarda los que fueron devueltos
                Row fila = hoja.createRow(filaActual++);
                fila.createCell(0).setCellValue(a.getIdAdopcion());
                fila.createCell(1).setCellValue(a.getIdMascota());
                fila.createCell(2).setCellValue(a.getDniAdoptante());
                fila.createCell(3).setCellValue(a.getMotivoDevolucion());
                fila.createCell(4).setCellValue(a.getDetalleDevolucion() != null ? a.getDetalleDevolucion() : "");
            }
        }
        guardarArchivo(libro, "Devoluciones.xlsx");
    }

    private void guardarArchivo(Workbook libro, String nombreArchivo) {
        try {
            File archivoFinal = new File(RUTA_CARPETA, nombreArchivo);
            FileOutputStream salida = new FileOutputStream(archivoFinal);
            libro.write(salida);
            salida.close();
            libro.close();
            System.out.println("💾 Datos exportados correctamente en: " + archivoFinal.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("❌ Error al guardar " + nombreArchivo + ": " + e.getMessage());
        }
    }
} 
*/