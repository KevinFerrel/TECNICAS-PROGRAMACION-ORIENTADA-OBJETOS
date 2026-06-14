package vista;

import java.time.LocalDate;
import controlador.*;
import persistencia.*;

public class MenuPrincipal {
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SISTEMA DE GESTIÓN DE REFUGIO DE MASCOTAS");
        System.out.println("=================================================\n");

        // 1. Instanciación de Controladores (Capa de Lógica)
        SeguridadController seguridad = new SeguridadController();
        MascotaController animalesController = new MascotaController();
        AdoptanteController personasController = new AdoptanteController();
        AdopcionController tramiteController = new AdopcionController(animalesController);
        
        // 2. Instanciación de Controladores (Capa de Persistencia)
        ExcelManager excel = new ExcelManager();
        AccesoDirectoManager accesos = new AccesoDirectoManager();

        // SIMULACIÓN DE FLUJO DE TRABAJO REAL

        // Paso A: Autenticación
        seguridad.login("admin", "admin123");

        // Paso B: Ingreso de Animales (RF01)
        System.out.println("\n--- REGISTRO DE MASCOTAS ---");
        animalesController.registrarMascota("M-001", "Max", "Perro", "Mestizo", LocalDate.of(2023, 10, 1));
        animalesController.registrarMascota("M-002", "Luna", "Gato", "Siamés", LocalDate.of(2024, 1, 15));

        // Paso C: Evaluación de Solicitantes (RF24 y Cuadro de Rechazo)
        System.out.println("\n--- REGISTRO Y EVALUACIÓN DE ADOPTANTES ---");
        personasController.registrarAdoptante("45556666", "Roberto Sanchez", 35, "987654321");
        // Intentar registrar el mismo DNI dispara la alerta de duplicidad:
        personasController.registrarAdoptante("45556666", "Roberto Sanchez", 35, "987654321");
        
        personasController.registrarAdoptante("77889900", "Ana Torres", 28, "999111222");
        
        // Aprobamos a Roberto y rechazamos a Ana con motivo obligatorio
        personasController.evaluarAdoptante("45556666", "Aprobado", "");
        personasController.evaluarAdoptante("77889900", "Rechazado", "Inestabilidad laboral y vivienda no apta.");

        // Paso D: Proceso de Adopción (RF32 y RF33)
        System.out.println("\n--- TRÁMITE DE ADOPCIÓN ---");
        var expediente = tramiteController.generarSolicitudAdopcion("45556666", "M-001");
        tramiteController.concretarAdopcion(expediente.getIdAdopcion(), LocalDate.now().plusDays(2));

        // Paso E: Reversión / Devolución empleando el Catálogo (RF38)
        System.out.println("\n--- DEVOLUCIÓN DE MASCOTA ---");
        // Se ejecuta una reversión por el Motivo #3 (Cambio de domicilio)
        tramiteController.procesarDevolucionMascota(expediente.getIdAdopcion(), 3, "El adoptante se muda fuera del país por trabajo.");

        // Paso F: Guardado en Disco
        System.out.println("\n--- EXPORTACIÓN DE DATOS (EXCEL) ---");
        excel.respaldarMascotas(animalesController.listarTodas());
        excel.respaldarAdoptantes(personasController.obtenerTodos());
        excel.respaldarDevoluciones(tramiteController.obtenerTodas()); // Genera el nuevo excel
        
        // Paso G: Accesos directos
        accesos.crearAccesosDirectos();

        // Paso H: Cierre
        System.out.println("\n--- DESCONEXIÓN ---");
        seguridad.logout();
    }
}