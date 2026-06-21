package vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import controlador.*;
import persistencia.*;

public class MenuPrincipal {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   SISTEMA DE GESTIÓN DE REFUGIO DE MASCOTAS");
        System.out.println("===========================================\n");

        SeguridadController seguridad = new SeguridadController();
        MascotaController animalesController = new MascotaController();
        AdoptanteController personasController = new AdoptanteController();
        AdopcionController tramiteController = new AdopcionController(animalesController);
        HistorialMedicoController historialController = new HistorialMedicoController();

        PersistenciaManager.cargarDatos(animalesController, personasController, tramiteController, historialController);

        seguridad.login("admin", "admin123");

        System.out.println("\n--- REGISTRO DE MASCOTAS ---");
        animalesController.registrarMascota("M-001", "Max", "Perro", "Mestizo", LocalDate.of(2023, 10, 1));
        animalesController.registrarMascota("M-002", "Luna", "Gato", "Siamés", LocalDate.of(2024, 1, 15));

        System.out.println("\n--- REGISTRO Y EVALUACIÓN DE ADOPTANTES ---");
        personasController.registrarAdoptante("45556666", "Roberto Sanchez", 35, "987654321");
        personasController.registrarAdoptante("45556666", "Roberto Sanchez", 35, "987654321");
        personasController.registrarAdoptante("77889900", "Ana Torres", 28, "999111222");
        personasController.evaluarAdoptante("45556666", "Aprobado", "");
        personasController.evaluarAdoptante("77889900", "Rechazado", "Inestabilidad laboral y vivienda no apta.");

        System.out.println("\n--- TRÁMITE DE ADOPCIÓN ---");
        var expediente = tramiteController.generarSolicitudAdopcion("45556666", "M-001");
        if (expediente != null) {
            tramiteController.concretarAdopcion(expediente.getIdAdopcion(), LocalDate.now().plusDays(2));
        }

        System.out.println("\n--- HISTORIAL MÉDICO ---");
        var cita = historialController.programarCita("M-001", LocalDateTime.now().plusDays(1));
        if (cita != null) {
            historialController.registrarPesoTalla(cita.getIdCita(), 5.2, 30.0);
            historialController.registrarDiagnostico(cita.getIdCita(), "Revisión general, sano.");
        }

        System.out.println("\n--- DEVOLUCIÓN DE MASCOTA ---");
        if (expediente != null) {
            tramiteController.procesarDevolucionMascota(expediente.getIdAdopcion(), 3, "Se muda fuera del país.");
        } else {
            System.out.println("No hay adopción para devolver.");
        }

        System.out.println("\n--- EXPORTACIÓN A EXCEL ---");
        ExcelManager excel = new ExcelManager();
        excel.respaldarMascotas(animalesController.listarTodas());
        excel.respaldarAdoptantes(personasController.obtenerTodos());
        excel.respaldarDevoluciones(tramiteController.obtenerDevoluciones());

        System.out.println("\n--- ACCESOS DIRECTOS ---");
        AccesoDirectoManager accesos = new AccesoDirectoManager();
        accesos.crearAccesosDirectos();

        PersistenciaManager.guardarDatos(animalesController, personasController, tramiteController, historialController);

        System.out.println("\n--- DESCONEXIÓN ---");
        seguridad.logout();
    }
}