package persistencia;

//import java.io.File;
//import mslinks.ShellLink; de momento no lo usaremos

public class AccesoDirectoManager {

    public void crearAccesosDirectos() {
        System.out.println("Accesos directos pendientes de implementación.");
        System.out.println("Para activar esta función se necesita agregar mslinks.jar al Build Path.");
    }
}
/*Codigo por implementar.
 * public class AccesoDirectoManager {
     
    public void crearAccesosDirectos() {
        try {
            // Se localiza la ruta absoluta del escritorio del sistema operativo anfitrión
            String rutaEscritorio = System.getProperty("user.home") + File.separator + "Desktop";
            
            // Acceso para Mascotas
            File excelMascotas = new File("datos/Mascotas.xlsx");
            if(excelMascotas.exists()) {
                ShellLink.createLink(excelMascotas.getAbsolutePath(), rutaEscritorio + File.separator + "Registro_Mascotas.lnk");
            }
            
            // Acceso para Devoluciones
            File excelDevoluciones = new File("datos/Devoluciones.xlsx");
            if(excelDevoluciones.exists()) {
                ShellLink.createLink(excelDevoluciones.getAbsolutePath(), rutaEscritorio + File.separator + "Control_Devoluciones.lnk");
            }
            
            System.out.println("🔗 Accesos directos (.lnk) creados en el Escritorio exitosamente.");
            
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudieron generar los accesos directos. Asegúrese de incluir mslinks.jar en el Build Path.");
        }
    }
    */
