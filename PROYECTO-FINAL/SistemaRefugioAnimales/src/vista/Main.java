package vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import controlador.*;
import modelo.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final SeguridadController seguridad = new SeguridadController();
    private static final MascotaController mascotaController = new MascotaController();
    private static final AdoptanteController adoptanteController = new AdoptanteController();
    private static final AdopcionController adopcionController = new AdopcionController(mascotaController);
    private static final HistorialMedicoController historialController = new HistorialMedicoController();

    public static void main(String[] args) {
        // Cargar datos
        PersistenciaManager.cargarDatos(mascotaController, adoptanteController, adopcionController, historialController);

        // ======== REVISAR RECORDATORIOS DE VISITAS ========
        revisarRecordatoriosVisitas();

        // Login con reintentos
        while (!login()) {
            System.out.println("❌ Credenciales incorrectas. Intente nuevamente.");
            pausa();
        }

        // Menú principal
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion(1, 6);

            switch (opcion) {
                case 1 -> gestionarMascotas();
                case 2 -> gestionarAdoptantes();
                case 3 -> gestionarAdopciones();
                case 4 -> gestionarHistorialMedico();
                case 5 -> {
                    System.out.println("\n--- GUARDANDO DATOS ---");
                    guardarDatos();
                    pausa();
                }
                case 6 -> {
                    System.out.println("\n--- GUARDANDO DATOS ANTES DE SALIR ---");
                    guardarDatos();
                    System.out.println("👋 Saliendo del sistema...");
                }
            }
        } while (opcion != 6);

        sc.close();
    }

    // ==================== RECORDATORIO DE VISITAS ====================

    private static void revisarRecordatoriosVisitas() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Adopcion> concretadas = adopcionController.obtenerSolicitudesConcretadas();
        boolean hayRecordatorio = false;
        for (Adopcion a : concretadas) {
            VisitaSeguimiento v1 = a.getVisita1();
            VisitaSeguimiento v2 = a.getVisita2();
            if (v1 != null && v1.getFechaVisita().equals(manana) && !v1.isNotificado()) {
                System.out.println("\n🔔 RECORDATORIO: Visita de seguimiento mañana para adopción " + a.getIdAdopcion());
                System.out.println("   Adoptante: " + a.getDniAdoptante() + " - Mascota: " + a.getIdMascota());
                System.out.println("   Hora: " + v1.getHoraVisita());
                v1.setNotificado(true);
                hayRecordatorio = true;
            }
            if (v2 != null && v2.getFechaVisita().equals(manana) && !v2.isNotificado()) {
                System.out.println("\n🔔 RECORDATORIO: Segunda visita de seguimiento mañana para adopción " + a.getIdAdopcion());
                System.out.println("   Adoptante: " + a.getDniAdoptante() + " - Mascota: " + a.getIdMascota());
                System.out.println("   Hora: " + v2.getHoraVisita());
                v2.setNotificado(true);
                hayRecordatorio = true;
            }
        }
        if (hayRecordatorio) {
            System.out.print("Presione Enter para continuar...");
            sc.nextLine();
        }
    }

    // ==================== UTILIDADES ====================

    private static void limpiarConsola() {
        for (int i = 0; i < 30; i++) System.out.println();
    }

    private static void pausa() {
        System.out.print("\nPresione Enter para continuar...");
        sc.nextLine();
    }

    private static int leerOpcion(int min, int max) {
        int opcion;
        while (true) {
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
                if (opcion >= min && opcion <= max) return opcion;
                System.out.printf("❌ Ingrese un número entre %d y %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("❌ Ingrese un número válido: ");
            }
        }
    }

    private static String leerString(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número válido.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        LocalDate fecha = null;
        while (fecha == null) {
            System.out.print(mensaje);
            try {
                fecha = LocalDate.parse(sc.nextLine(), FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato incorrecto. Use dd/MM/yyyy (ej: 15/12/2024)");
            }
        }
        return fecha;
    }

    private static LocalTime leerHora() {
        LocalTime hora = null;
        while (hora == null) {
            System.out.print("Hora (HH:mm): ");
            try {
                hora = LocalTime.parse(sc.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Formato de hora incorrecto. Use HH:mm (ej: 14:30)");
            }
        }
        return hora;
    }

    private static boolean login() {
        limpiarConsola();
        System.out.println("===========================================");
        System.out.println("   SISTEMA DE GESTIÓN DE REFUGIO DE MASCOTAS");
        System.out.println("===========================================\n");
        String usuario = leerString("👤 Usuario: ");
        String pass = leerString("🔒 Contraseña: ");
        return seguridad.login(usuario, pass);
    }

    private static void guardarDatos() {
        PersistenciaManager.guardarDatos(mascotaController, adoptanteController, adopcionController, historialController);
        System.out.println("✅ Datos guardados correctamente.");
    }

    // ==================== MÉTODOS GENÉRICOS ====================

    private static <T> void mostrarLista(String titulo, List<T> lista, Function<T, String> formato) {
        if (lista.isEmpty()) {
            System.out.println("No hay elementos para mostrar.");
            return;
        }
        System.out.println("\n--- " + titulo + " ---");
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, formato.apply(lista.get(i)));
        }
    }

    private static <T> T seleccionarDeLista(String mensaje, List<T> lista, Function<T, String> formato) {
        mostrarLista("", lista, formato);
        if (lista.isEmpty()) return null;
        System.out.print(mensaje + " (0 para cancelar): ");
        int idx = leerOpcion(0, lista.size());
        return idx == 0 ? null : lista.get(idx - 1);
    }

    // ==================== MENÚS ====================

    private static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENÚ PRINCIPAL =========");
        System.out.println("1. 🐾 Gestión de Mascotas");
        System.out.println("2. 👤 Gestión de Adoptantes");
        System.out.println("3. 📋 Gestión de Adopciones");
        System.out.println("4. 🏥 Historial Médico");
        System.out.println("5. 💾 Guardar datos");
        System.out.println("6. 🚪 Salir");
        System.out.print("Opción: ");
    }

    private static void mostrarSubMenuMascotas() {
        System.out.println("\n--- GESTIÓN DE MASCOTAS ---");
        System.out.println("1. Registrar mascota");
        System.out.println("2. Listar todas las mascotas");
        System.out.println("3. Listar mascotas disponibles");
        System.out.println("4. Buscar mascota por ID");
        System.out.println("5. Cambiar estado de mascota");
        System.out.println("6. Registrar descripción física");
        System.out.println("7. Registrar observaciones de conducta");
        System.out.println("8. Volver al menú principal");
        System.out.print("Opción: ");
    }

    private static void mostrarSubMenuAdoptantes() {
        System.out.println("\n--- GESTIÓN DE ADOPTANTES ---");
        System.out.println("1. Registrar adoptante");
        System.out.println("2. Listar todos los adoptantes");
        System.out.println("3. Buscar adoptante por DNI");
        System.out.println("4. Evaluar adoptante (Aprobar/Rechazar)");
        System.out.println("5. Reevaluar adoptante (cambiar estado)");
        System.out.println("6. Volver al menú principal");
        System.out.print("Opción: ");
    }

    private static void mostrarSubMenuAdopciones() {
        System.out.println("\n--- GESTIÓN DE ADOPCIONES ---");
        System.out.println("1. Generar solicitud de adopción");
        System.out.println("2. Concretar adopción (ver lista de espera)");
        System.out.println("3. Procesar devolución de mascota");
        System.out.println("4. Programar visita de seguimiento");
        System.out.println("5. Listar adopciones detalladas");
        System.out.println("6. Volver al menú principal");
        System.out.print("Opción: ");
    }

    private static void mostrarSubMenuHistorial() {
        System.out.println("\n--- HISTORIAL MÉDICO ---");
        System.out.println("1. Programar cita médica");
        System.out.println("2. Registrar diagnóstico en cita");
        System.out.println("3. Registrar tratamiento");
        System.out.println("4. Registrar vacuna");
        System.out.println("5. Ver resumen clínico");
        System.out.println("6. Volver al menú principal");
        System.out.print("Opción: ");
    }

    // ==================== GESTIÓN DE MASCOTAS ====================

    private static void gestionarMascotas() {
        int opcion;
        do {
            mostrarSubMenuMascotas();
            opcion = leerOpcion(1, 8);
            switch (opcion) {
                case 1 -> registrarMascota();
                case 2 -> listarTodasMascotas();
                case 3 -> listarMascotasDisponibles();
                case 4 -> buscarMascotaPorId();
                case 5 -> cambiarEstadoMascota();
                case 6 -> registrarDescripcionFisica();
                case 7 -> registrarObservacionesConducta();
                case 8 -> System.out.println("Volviendo al menú principal...");
            }
            if (opcion != 8) pausa();
        } while (opcion != 8);
    }

    private static void registrarMascota() {
        String id = leerString("ID: ");
        String nombre = leerString("Nombre: ");
        String especie = leerString("Especie: ");
        String raza = leerString("Raza: ");
        LocalDate fecha = leerFecha("Fecha de rescate (dd/MM/yyyy): ");

        if (mascotaController.registrarMascota(id, nombre, especie, raza, fecha)) {
            System.out.println("✔ Mascota registrada correctamente.");
            guardarDatos();
        }
    }

    private static void listarTodasMascotas() {
        mostrarLista("LISTA DE MASCOTAS", mascotaController.listarTodas(), Mascota::toString);
    }

    private static void listarMascotasDisponibles() {
        mostrarLista("MASCOTAS DISPONIBLES", mascotaController.listarDisponibles(), Mascota::toString);
    }

    private static void buscarMascotaPorId() {
        String id = leerString("ID de la mascota: ");
        Mascota m = mascotaController.buscarPorId(id);
        System.out.println(m == null ? "❌ Mascota no encontrada." : m);
    }

    private static void cambiarEstadoMascota() {
        String id = leerString("ID de la mascota: ");
        Mascota m = mascotaController.buscarPorId(id);
        if (m == null) {
            System.out.println("❌ Mascota no encontrada.");
            return;
        }

        System.out.println("Seleccione el nuevo estado:");
        System.out.println("1. Disponible");
        System.out.println("2. En cuarentena");
        System.out.println("3. En tratamiento");
        System.out.println("4. Adoptada");
        int opcion = leerOpcion(1, 4);
        String[] estados = {"Disponible", "En cuarentena", "En tratamiento", "Adoptada"};
        String nuevoEstado = estados[opcion - 1];

        if (mascotaController.actualizarEstado(id, nuevoEstado)) {
            System.out.println("✔ Estado actualizado a '" + nuevoEstado + "'.");
            guardarDatos();
        }
    }

    private static void registrarDescripcionFisica() {
        String id = leerString("ID de la mascota: ");
        String color = leerString("Color: ");
        String tamano = leerString("Tamaño (Pequeño/Mediano/Grande): ");
        String marcas = leerString("Marcas particulares: ");
        if (mascotaController.registrarDescripcionFisica(id, color, tamano, marcas)) {
            System.out.println("✔ Descripción registrada.");
            guardarDatos();
        }
    }

    private static void registrarObservacionesConducta() {
        String id = leerString("ID de la mascota: ");
        String obs = leerString("Observaciones de conducta: ");
        if (mascotaController.registrarObservacionesConducta(id, obs)) {
            System.out.println("✔ Observaciones registradas.");
            guardarDatos();
        }
    }

    // ==================== GESTIÓN DE ADOPTANTES ====================

    private static void gestionarAdoptantes() {
        int opcion;
        do {
            mostrarSubMenuAdoptantes();
            opcion = leerOpcion(1, 6);
            switch (opcion) {
                case 1 -> registrarAdoptante();
                case 2 -> listarAdoptantes();
                case 3 -> buscarAdoptante();
                case 4 -> evaluarAdoptante();
                case 5 -> reevaluarAdoptante();
                case 6 -> System.out.println("Volviendo al menú principal...");
            }
            if (opcion != 6) pausa();
        } while (opcion != 6);
    }

    private static void registrarAdoptante() {
        String dni = leerString("DNI: ");
        String nombre = leerString("Nombre completo: ");
        int edad = leerInt("Edad: ");
        String telefono = leerString("Teléfono: ");
        String email = leerString("Correo electrónico: ");

        adoptanteController.registrarAdoptante(dni, nombre, edad, telefono, email);
        guardarDatos();
    }

    private static void listarAdoptantes() {
        List<Adoptante> lista = adoptanteController.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay adoptantes registrados.");
            return;
        }
        System.out.println("\n--- LISTA DE ADOPTANTES ---");
        for (Adoptante a : lista) {
            System.out.println("DNI: " + a.getDni());
            System.out.println("Nombre: " + a.getNombre());
            System.out.println("Edad: " + a.getEdad());
            System.out.println("Teléfono: " + a.getTelefono());
            System.out.println("Email: " + a.getEmail());
            System.out.println("Estado: " + a.getEstadoValidacion());
            if (a.getMotivoRechazo() != null && !a.getMotivoRechazo().isEmpty()) {
                System.out.println("⚠️ Motivo de rechazo: " + a.getMotivoRechazo());
            }
            System.out.println("----------------------------");
        }
    }

    private static void buscarAdoptante() {
        String dni = leerString("DNI del adoptante: ");
        Adoptante a = adoptanteController.buscarPorDni(dni);
        if (a == null) {
            System.out.println("❌ Adoptante no encontrado.");
            return;
        }
        System.out.println("DNI: " + a.getDni());
        System.out.println("Nombre: " + a.getNombre());
        System.out.println("Edad: " + a.getEdad());
        System.out.println("Teléfono: " + a.getTelefono());
        System.out.println("Email: " + a.getEmail());
        System.out.println("Estado: " + a.getEstadoValidacion());
        if (a.getMotivoRechazo() != null && !a.getMotivoRechazo().isEmpty()) {
            System.out.println("Motivo de rechazo: " + a.getMotivoRechazo());
        }
    }

    private static void evaluarAdoptante() {
        String dni = leerString("DNI del adoptante: ");
        Adoptante a = adoptanteController.buscarPorDni(dni);
        if (a == null) {
            System.out.println("❌ Adoptante no encontrado.");
            return;
        }

        System.out.println("Estado actual: " + a.getEstadoValidacion());
        System.out.println("Seleccione el nuevo estado:");
        System.out.println("1. Aprobado");
        System.out.println("2. Rechazado");
        int opcion = leerOpcion(1, 2);
        String estado = opcion == 1 ? "Aprobado" : "Rechazado";

        String motivo = "";
        if (estado.equals("Rechazado")) {
            motivo = leerString("Motivo del rechazo (obligatorio): ");
            while (motivo.trim().isEmpty()) {
                System.out.println("⚠️ El motivo es obligatorio.");
                motivo = leerString("Ingrese el motivo: ");
            }
        }

        if (adoptanteController.evaluarAdoptante(dni, estado, motivo)) {
            System.out.println("✔ Evaluación registrada.");
            guardarDatos();
        }
    }

    private static void reevaluarAdoptante() {
        String dni = leerString("DNI del adoptante a reevaluar: ");
        Adoptante a = adoptanteController.buscarPorDni(dni);
        if (a == null) {
            System.out.println("❌ Adoptante no encontrado.");
            return;
        }

        System.out.println("Estado actual: " + a.getEstadoValidacion());
        if (a.getMotivoRechazo() != null && !a.getMotivoRechazo().isEmpty()) {
            System.out.println("Motivo de rechazo anterior: " + a.getMotivoRechazo());
        }

        System.out.println("Seleccione el nuevo estado:");
        System.out.println("1. Aprobado");
        System.out.println("2. Rechazado");
        int opcion = leerOpcion(1, 2);
        String estado = opcion == 1 ? "Aprobado" : "Rechazado";

        String motivo = "";
        if (estado.equals("Rechazado")) {
            motivo = leerString("Motivo del rechazo (obligatorio): ");
            while (motivo.trim().isEmpty()) {
                System.out.println("⚠️ El motivo es obligatorio.");
                motivo = leerString("Ingrese el motivo: ");
            }
        }

        if (adoptanteController.evaluarAdoptante(dni, estado, motivo)) {
            System.out.println("✔ Estado actualizado a '" + estado + "'.");
            guardarDatos();
        }
    }

    // ==================== GESTIÓN DE ADOPCIONES (MEJORADA) ====================

    private static void gestionarAdopciones() {
        int opcion;
        do {
            mostrarSubMenuAdopciones();
            opcion = leerOpcion(1, 6);
            switch (opcion) {
                case 1 -> generarSolicitud();
                case 2 -> concretarAdopcion();
                case 3 -> procesarDevolucionDetallada();
                case 4 -> programarVisitaConHora();
                case 5 -> listarAdopcionesDetalladas();
                case 6 -> System.out.println("Volviendo al menú principal...");
            }
            if (opcion != 6) pausa();
        } while (opcion != 6);
    }

    // ======== GENERAR SOLICITUD ========

    private static void generarSolicitud() {
        List<Adoptante> aprobados = adoptanteController.obtenerAprobados();
        Adoptante adoptante = seleccionarDeLista(
            "Seleccione el número del adoptante",
            aprobados,
            a -> a.getDni() + " - " + a.getNombre() + " (Tel: " + a.getTelefono() + ")"
        );
        if (adoptante == null) return;

        List<Mascota> disponibles = mascotaController.obtenerDisponibles();
        Mascota mascota = seleccionarDeLista(
            "Seleccione el número de la mascota",
            disponibles,
            m -> m.getIdMascota() + " - " + m.getNombre() + " (" + m.getEspecie() + ")"
        );
        if (mascota == null) return;

        Adopcion solicitud = adopcionController.generarSolicitudAdopcion(adoptante.getDni(), mascota.getIdMascota());
        if (solicitud != null) {
            System.out.println("✔ Solicitud generada: " + solicitud.getIdAdopcion());
            guardarDatos();

            if (leerString("¿Desea concretar la adopción ahora? (s/n): ").equalsIgnoreCase("s")) {
                concretarAdopcion(solicitud.getIdAdopcion());
            }
        }
    }

    // ======== CONCRETAR ADOPCIÓN ========

    private static void concretarAdopcion() {
        List<Adopcion> enProceso = adopcionController.obtenerSolicitudesEnProceso();
        if (enProceso.isEmpty()) {
            System.out.println("No hay solicitudes en proceso de adopción.");
            return;
        }

        System.out.println("\n--- LISTA DE SOLICITUDES EN ESPERA DE CONCRETAR ---");
        System.out.printf("%-3s %-12s %-25s %-12s %-25s %-15s%n", 
            "Nº", "DNI", "Adoptante", "Teléfono", "Email", "Mascota");
        System.out.println("--------------------------------------------------------------------------------------------");
        for (int i = 0; i < enProceso.size(); i++) {
            Adopcion a = enProceso.get(i);
            Adoptante adoptante = adoptanteController.buscarPorDni(a.getDniAdoptante());
            Mascota mascota = mascotaController.buscarPorId(a.getIdMascota());
            System.out.printf("%-3d %-12s %-25s %-12s %-25s %-15s%n", 
                (i+1),
                adoptante != null ? adoptante.getDni() : "N/A",
                adoptante != null ? adoptante.getNombre() : "N/A",
                adoptante != null ? adoptante.getTelefono() : "N/A",
                adoptante != null ? adoptante.getEmail() : "N/A",
                mascota != null ? mascota.getNombre() : "N/A"
            );
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.print("Seleccione el número de la solicitud (0 para cancelar): ");
        int idx = leerOpcion(0, enProceso.size());
        if (idx == 0) return;
        Adopcion solicitud = enProceso.get(idx - 1);
        concretarAdopcion(solicitud.getIdAdopcion());
    }

    private static void concretarAdopcion(String idSolicitud) {
        Adopcion solicitud = adopcionController.buscarPorId(idSolicitud);
        if (solicitud == null || !solicitud.getEstado().equals("En proceso")) {
            System.out.println("❌ Solicitud no válida.");
            return;
        }

        Adoptante adoptante = adoptanteController.buscarPorDni(solicitud.getDniAdoptante());
        Mascota mascota = mascotaController.buscarPorId(solicitud.getIdMascota());

        System.out.println("\n========== DATOS DE LA ADOPCIÓN ==========");
        System.out.println("📌 ADOPTANTE:");
        System.out.println("   DNI: " + adoptante.getDni());
        System.out.println("   Nombre: " + adoptante.getNombre());
        System.out.println("   Teléfono: " + adoptante.getTelefono());
        System.out.println("   Email: " + adoptante.getEmail());
        System.out.println("   Edad: " + adoptante.getEdad());
        System.out.println("   Dirección: " + (adoptante.getDireccion() != null ? adoptante.getDireccion() : "No registrada"));
        System.out.println("   Preferencias: " + (adoptante.getPreferencias() != null ? adoptante.getPreferencias() : "No registradas"));
        System.out.println("   Tipo de vivienda: " + (adoptante.getTipoVivienda() != null ? adoptante.getTipoVivienda() : "No registrado"));
        System.out.println("\n🐾 MASCOTA:");
        System.out.println("   ID: " + mascota.getIdMascota());
        System.out.println("   Nombre: " + mascota.getNombre());
        System.out.println("   Especie: " + mascota.getEspecie());
        System.out.println("   Raza: " + mascota.getRaza());
        System.out.println("   Color: " + (mascota.getColor() != null ? mascota.getColor() : "No registrado"));
        System.out.println("   Tamaño: " + (mascota.getTamano() != null ? mascota.getTamano() : "No registrado"));
        System.out.println("===========================================");

        System.out.println("\n📞 CONTACTO RÁPIDO:");
        System.out.println("   WhatsApp: https://wa.me/" + adoptante.getTelefono().replaceAll("[^0-9]", ""));
        System.out.println("   Email: mailto:" + adoptante.getEmail());

        LocalDate fecha = leerFecha("\nFecha de entrega (dd/MM/yyyy): ");

        if (adopcionController.concretarAdopcion(idSolicitud, fecha)) {
            System.out.println("✔ Adopción concretada exitosamente.");
            guardarDatos();

            if (leerString("¿Desea programar la primera visita de seguimiento (30 días después)? (s/n): ").equalsIgnoreCase("s")) {
                LocalDate fechaVisita = fecha.plusDays(30);
                LocalTime hora = leerHora();
                adopcionController.programarVisitaSeguimiento(idSolicitud, fechaVisita, hora);
                System.out.println("✔ Visita de seguimiento programada para: " + fechaVisita + " a las " + hora);
                guardarDatos();
            }
        } else {
            System.out.println("❌ No se pudo concretar la adopción.");
        }
    }

    // ======== PROGRAMAR VISITA CON HORA ========

    private static void programarVisitaConHora() {
        List<Adopcion> concretadas = adopcionController.obtenerSolicitudesConcretadas();
        if (concretadas.isEmpty()) {
            System.out.println("No hay adopciones concretadas para programar visitas.");
            return;
        }

        System.out.println("\n--- ADOPCIONES CONCRETADAS ---");
        System.out.printf("%-3s %-10s %-12s %-15s%n", "Nº", "ID", "DNI Adoptante", "Mascota");
        for (int i = 0; i < concretadas.size(); i++) {
            Adopcion a = concretadas.get(i);
            System.out.printf("%-3d %-10s %-12s %-15s%n", 
                (i+1), a.getIdAdopcion(), a.getDniAdoptante(), a.getIdMascota());
        }
        System.out.print("Seleccione el número de la adopción (0 para cancelar): ");
        int idx = leerOpcion(0, concretadas.size());
        if (idx == 0) return;
        Adopcion adopcion = concretadas.get(idx - 1);

        Adoptante adoptante = adoptanteController.buscarPorDni(adopcion.getDniAdoptante());
        if (adoptante == null) {
            System.out.println("❌ No se encontró al adoptante.");
            return;
        }

        LocalDate fecha = leerFecha("Fecha de visita (dd/MM/yyyy): ");
        LocalTime hora = leerHora();

        if (adopcionController.programarVisitaSeguimiento(adopcion.getIdAdopcion(), fecha, hora)) {
            System.out.println("✔ Visita programada.");

            // SIMULAR ENVÍO DE CORREO
            System.out.println("\n📧 ENVIANDO CORREO A: " + adoptante.getEmail());
            System.out.println("   Asunto: Visita de seguimiento - Refugio de Mascotas");
            System.out.println("   Mensaje: Estimado/a " + adoptante.getNombre() + ", le informamos que se ha programado una visita de seguimiento para la adopción de su mascota.");
            System.out.println("   Fecha: " + fecha + " a las " + hora);
            System.out.println("   Por favor, confirme su asistencia.");

            // SIMULAR ENVÍO DE WHATSAPP
            System.out.println("\n📱 ENVIANDO WHATSAPP A: " + adoptante.getTelefono());
            System.out.println("   Mensaje: Hola " + adoptante.getNombre() + ", le recordamos la visita de seguimiento el " + fecha + " a las " + hora + ". ¡Gracias!");

            guardarDatos();
        }
    }

    // ======== PROCESAR DEVOLUCIÓN DETALLADA ========

    private static void procesarDevolucionDetallada() {
        List<Adopcion> concretadas = adopcionController.obtenerSolicitudesConcretadas();
        if (concretadas.isEmpty()) {
            System.out.println("No hay adopciones concretadas para devolver.");
            return;
        }

        System.out.println("\n--- ADOPCIONES CONCRETADAS ---");
        System.out.printf("%-3s %-10s %-12s %-15s%n", "Nº", "ID", "DNI Adoptante", "Mascota");
        for (int i = 0; i < concretadas.size(); i++) {
            Adopcion a = concretadas.get(i);
            System.out.printf("%-3d %-10s %-12s %-15s%n", 
                (i+1), a.getIdAdopcion(), a.getDniAdoptante(), a.getIdMascota());
        }
        System.out.print("Seleccione el número de la adopción (0 para cancelar): ");
        int idx = leerOpcion(0, concretadas.size());
        if (idx == 0) return;
        Adopcion adopcion = concretadas.get(idx - 1);

        Adoptante adoptante = adoptanteController.buscarPorDni(adopcion.getDniAdoptante());
        Mascota mascota = mascotaController.buscarPorId(adopcion.getIdMascota());

        System.out.println("\n========== REGISTRO DE DEVOLUCIÓN ==========");
        System.out.println("📌 ADOPTANTE:");
        System.out.println("   DNI: " + adoptante.getDni());
        System.out.println("   Nombre: " + adoptante.getNombre());
        System.out.println("   Teléfono: " + adoptante.getTelefono());
        System.out.println("   Email: " + adoptante.getEmail());
        System.out.println("   Dirección: " + (adoptante.getDireccion() != null ? adoptante.getDireccion() : "No registrada"));
        System.out.println("\n🐾 MASCOTA:");
        System.out.println("   ID: " + mascota.getIdMascota());
        System.out.println("   Nombre: " + mascota.getNombre());
        System.out.println("   Especie: " + mascota.getEspecie());
        System.out.println("   Raza: " + mascota.getRaza());
        System.out.println("   Color: " + (mascota.getColor() != null ? mascota.getColor() : "No registrado"));
        System.out.println("   Tamaño: " + (mascota.getTamano() != null ? mascota.getTamano() : "No registrado"));
        System.out.println("===========================================");

        System.out.println("\nSeleccione el motivo principal de devolución:");
        String[] motivos = {
            "Problemas de comportamiento",
            "Problemas de salud",
            "Cambio de domicilio",
            "Problemas económicos",
            "Incompatibilidad con otros animales",
            "Incompatibilidad con niños",
            "Fallecimiento del adoptante",
            "Otro motivo"
        };
        for (int i = 0; i < motivos.length; i++) {
            System.out.println((i + 1) + ". " + motivos[i]);
        }
        int motivo = leerOpcion(1, motivos.length);
        System.out.print("Detalle adicional: ");
        String detalle = sc.nextLine();

        System.out.print("Descripción detallada del motivo (ej: 'Agresividad hacia niños'): ");
        String motivoDetallado = sc.nextLine();
        System.out.print("Acción a tomar (ej: 'Cuarentena', 'Entrenamiento', 'Evaluación veterinaria'): ");
        String accion = sc.nextLine();

        boolean cuarentena = false;
        if (motivo == 1 || motivo == 2) {
            System.out.print("¿Desea poner la mascota en cuarentena? (s/n): ");
            cuarentena = sc.nextLine().equalsIgnoreCase("s");
            if (cuarentena) {
                System.out.print("Indique si es por salud o conducta: ");
                String tipoCuarentena = sc.nextLine();
                accion += " - Cuarentena por " + tipoCuarentena;
            }
        }

        if (adopcionController.procesarDevolucionDetallada(adopcion.getIdAdopcion(), motivo, detalle, 
                                                            motivoDetallado, accion, cuarentena)) {
            System.out.println("✔ Devolución procesada y registrada.");
            guardarDatos();
        }
    }

    // ======== LISTAR ADOPCIONES DETALLADAS ========

    private static void listarAdopcionesDetalladas() {
        List<Adopcion> lista = adopcionController.obtenerTodas();
        if (lista.isEmpty()) {
            System.out.println("No hay solicitudes de adopción.");
            return;
        }

        System.out.println("\n--- SOLICITUDES DE ADOPCIÓN (DETALLADAS) ---");
        for (Adopcion a : lista) {
            Adoptante adoptante = adoptanteController.buscarPorDni(a.getDniAdoptante());
            Mascota mascota = mascotaController.buscarPorId(a.getIdMascota());

            System.out.println("ID: " + a.getIdAdopcion());
            System.out.println("Adoptante: " + (adoptante != null ? adoptante.getNombre() + " (DNI: " + adoptante.getDni() + ")" : "No encontrado"));
            System.out.println("Teléfono: " + (adoptante != null ? adoptante.getTelefono() : "N/A"));
            System.out.println("Email: " + (adoptante != null ? adoptante.getEmail() : "N/A"));
            System.out.println("Mascota: " + (mascota != null ? mascota.getNombre() + " (" + mascota.getIdMascota() + ")" : "No encontrada"));
            System.out.println("Estado: " + a.getEstado());
            System.out.println("Fecha solicitud: " + a.getFechaSolicitud());
            if (a.getEstado().equals("Concretada") || a.getEstado().equals("Revertida")) {
                System.out.println("Fecha entrega: " + a.getFechaEntrega());
                System.out.println("Fecha contrato: " + a.getFechaContrato());
            }
            if (a.isEsDevuelta()) {
                System.out.println("⚠️ DEVUELTA - Motivo: " + a.getMotivoDevolucion());
                System.out.println("   Detalle adicional: " + a.getDetalleDevolucion());
                System.out.println("   Motivo detallado: " + a.getMotivoDevolucionDetallado());
                System.out.println("   Acción tomada: " + a.getAccionTomada());
                if (a.isCuarentena()) {
                    System.out.println("   ⚠️ Mascota en cuarentena");
                }
            }
            if (a.getVisita1() != null) {
                System.out.println("Visita 1: " + a.getVisita1().getFechaVisita() + " a las " + a.getVisita1().getHoraVisita() + " - " + a.getVisita1().getObservaciones());
            }
            if (a.getVisita2() != null) {
                System.out.println("Visita 2: " + a.getVisita2().getFechaVisita() + " a las " + a.getVisita2().getHoraVisita() + " - " + a.getVisita2().getObservaciones());
            }
            System.out.println("----------------------------");
        }
    }

    // ==================== HISTORIAL MÉDICO ====================

    private static void gestionarHistorialMedico() {
        int opcion;
        do {
            mostrarSubMenuHistorial();
            opcion = leerOpcion(1, 6);
            switch (opcion) {
                case 1 -> programarCita();
                case 2 -> registrarDiagnostico();
                case 3 -> registrarTratamiento();
                case 4 -> registrarVacuna();
                case 5 -> verResumenClinico();
                case 6 -> System.out.println("Volviendo al menú principal...");
            }
            if (opcion != 6) pausa();
        } while (opcion != 6);
    }

    private static void programarCita() {
        String id = leerString("ID de la mascota: ");
        System.out.print("Fecha y hora (dd/MM/yyyy HH:mm): ");
        try {
            String[] partes = sc.nextLine().split(" ");
            LocalDate fecha = LocalDate.parse(partes[0], FORMATTER);
            String[] hora = partes[1].split(":");
            var cita = historialController.programarCita(id, fecha.atTime(Integer.parseInt(hora[0]), Integer.parseInt(hora[1])));
            System.out.println("✔ Cita programada: " + cita.getIdCita());
            guardarDatos();
        } catch (Exception e) {
            System.out.println("❌ Formato incorrecto. Use dd/MM/yyyy HH:mm (ej: 15/12/2024 14:30)");
        }
    }

    private static void registrarDiagnostico() {
        String id = leerString("ID de la cita: ");
        String diag = leerString("Diagnóstico: ");
        if (historialController.registrarDiagnostico(id, diag)) {
            System.out.println("✔ Diagnóstico registrado.");
            guardarDatos();
        }
    }

    private static void registrarTratamiento() {
        String id = leerString("ID de la mascota: ");
        String desc = leerString("Descripción del tratamiento: ");
        int dias = leerInt("Duración en días: ");

        var tratamiento = historialController.iniciarTratamientoMedico(id, desc, dias);
        System.out.println("✔ Tratamiento iniciado: " + tratamiento.getIdTratamiento());

        if (leerString("¿Desea agregar medicinas? (s/n): ").equalsIgnoreCase("s")) {
            while (true) {
                String med = leerString("Nombre de la medicina (o 'fin' para terminar): ");
                if (med.equalsIgnoreCase("fin")) break;
                String dosis = leerString("Dosis: ");
                historialController.asociarMedicinaATratamiento(tratamiento.getIdTratamiento(), med, dosis);
                System.out.println("✔ Medicina agregada.");
            }
        }
        guardarDatos();
    }

    private static void registrarVacuna() {
        String id = leerString("ID de la mascota: ");
        String nombre = leerString("Nombre de la vacuna: ");
        String lab = leerString("Laboratorio: ");
        LocalDate fecha = leerFecha("Fecha de aplicación (dd/MM/yyyy): ");

        historialController.registrarAplicacionVacuna(id, nombre, lab, fecha);
        System.out.println("✔ Vacuna registrada.");
        guardarDatos();
    }

    private static void verResumenClinico() {
        String id = leerString("ID de la mascota: ");
        System.out.println(historialController.generarResumenClinicoConsolidado(id));
    }
}