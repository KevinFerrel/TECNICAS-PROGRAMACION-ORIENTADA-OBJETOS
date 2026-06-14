package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Adopcion;
import modelo.VisitaSeguimiento;

public class AdopcionController {
    private List<Adopcion> expedientes = new ArrayList<>();
    private int contadorExpedientes = 1;
    
    // Dependencia inyectada para actualizar el estado del animal
    private MascotaController mascotaController;

    // Catálogo estricto de motivos de devolución para estandarizar el guardado
    public static final String[] CATALOGO_DEVOLUCIONES = {
        "1. No se adaptó al hogar",
        "2. Comportamiento violento / agresividad",
        "3. Cambio de domicilio del adoptante",
        "4. Problemas económicos del adoptante",
        "5. Alergias de algún miembro de la familia",
        "6. Incompatibilidad con otras mascotas",
        "7. Problemas de salud de la mascota",
        "8. Falta de tiempo para su cuidado",
        "9. Otro (especificar en detalle)"
    };

    // Constructor que exige el controlador de mascotas para operar
    public AdopcionController(MascotaController mascotaController) {
        this.mascotaController = mascotaController;
    }

    // RF32 y RF33: Generar la solicitud inicial e inmovilizar a la mascota en el inventario
    public Adopcion generarSolicitudAdopcion(String dniAdoptante, String idMascota) {
        String idGenerado = "EXP-" + (contadorExpedientes++);
        Adopcion nuevaSolicitud = new Adopcion(idGenerado, idMascota, dniAdoptante);
        expedientes.add(nuevaSolicitud);
        
        // Bloqueo de seguridad: Se cambia el estado para que nadie más la adopte temporalmente
        mascotaController.actualizarEstado(idMascota, "En proceso de adopción");
        System.out.println("✔ Solicitud generada exitosamente. Mascota bloqueada para otros procesos.");
        
        return nuevaSolicitud;
    }

    // RF34 y RF35: Concretar formalmente la firma y cambiar el estado final del animal
    public boolean concretarAdopcion(String idAdopcion, LocalDate fechaEntregaProgramada) {
        Adopcion expediente = buscarPorId(idAdopcion);
        if (expediente == null || !expediente.getEstado().equals("En proceso")) return false;

        // Se firma el contrato y se establece la fecha de retiro
        expediente.aprobarAdopcion(fechaEntregaProgramada);
        
        // Se ejecuta la salida lógica del animal del sistema de disponibles
        mascotaController.actualizarEstado(expediente.getIdMascota(), "Adoptada");
        return true;
    }

    // RF36: Crear cronograma de visitas post-entrega
    public boolean programarVisitaSeguimiento(String idAdopcion, LocalDate fechaVisita) {
        Adopcion expediente = buscarPorId(idAdopcion);
        if (expediente == null) return false;

        VisitaSeguimiento visita = new VisitaSeguimiento(fechaVisita);
        boolean programada = expediente.asignarVisita(visita);
        if (!programada) {
            System.out.println("Error: Ya se encuentran agendadas las 2 visitas máximas permitidas.");
        }
        return programada;
    }

    // RF37: Registrar resultados de la visita por parte del auditor del refugio
    public boolean registrarObservacionVisita(String idAdopcion, int numeroVisita, String observaciones) {
        Adopcion expediente = buscarPorId(idAdopcion);
        if (expediente == null) return false;

        if (numeroVisita == 1 && expediente.getVisita1() != null) {
            expediente.getVisita1().setObservaciones(observaciones);
            return true;
        } else if (numeroVisita == 2 && expediente.getVisita2() != null) {
            expediente.getVisita2().setObservaciones(observaciones);
            return true;
        }
        return false;
    }

    // RF38 y Restricción de Motivos: Ejecutar la reversión del contrato por uno de los 9 casos
    public boolean procesarDevolucionMascota(String idAdopcion, int numeroMotivo, String observacionesAdicionales) {
        Adopcion expediente = buscarPorId(idAdopcion);
        if (expediente == null) {
            System.out.println("Error: Número de expediente de adopción no localizado.");
            return false;
        }

        // Validación de índice del arreglo de motivos
        if (numeroMotivo < 1 || numeroMotivo > 9) {
            System.out.println("Error: Índice de motivo no válido.");
            return false;
        }

        String motivoOficial = CATALOGO_DEVOLUCIONES[numeroMotivo - 1];
        
        // Procedimiento de reversión del documento
        expediente.revertirAdopcion(motivoOficial, observacionesAdicionales);
        
        // Procedimiento de reintegración del animal
        mascotaController.actualizarEstado(expediente.getIdMascota(), "Disponible");
        
        System.out.println("✔ DEVOLUCIÓN ACEPTADA. La mascota [" + expediente.getIdMascota() + "] vuelve a estar Disponible.");
        System.out.println("  Motivo central: " + motivoOficial);
        return true;
    }

    // Método utilitario de búsqueda
    public Adopcion buscarPorId(String idAdopcion) {
        for (Adopcion a : expedientes) {
            if (a.getIdAdopcion().equalsIgnoreCase(idAdopcion)) {
                return a;
            }
        }
        return null;
    }

    // Método de extracción total de datos
    public List<Adopcion> obtenerTodas() {
        return expedientes;
    }
}