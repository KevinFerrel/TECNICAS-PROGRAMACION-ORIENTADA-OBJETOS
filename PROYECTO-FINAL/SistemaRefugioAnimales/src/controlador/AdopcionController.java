package controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Adopcion;
import modelo.VisitaSeguimiento;

public class AdopcionController {
    private List<Adopcion> listaAdopciones = new ArrayList<>();
    private MascotaController mascotaController;
    private int contadorExpedientes = 1;

    public AdopcionController(MascotaController mascotaController) {
        this.mascotaController = mascotaController;
    }

    // ============================================================
    // MÉTODOS ORIGINALES (YA EXISTÍAN)
    // ============================================================

    public Adopcion generarSolicitudAdopcion(String dniAdoptante, String idMascota) {
        var mascota = mascotaController.buscarPorId(idMascota);
        if (mascota == null || !mascota.getEstado().equals("Disponible")) {
            System.out.println("❌ La mascota no está disponible.");
            return null;
        }
        String id = "EXP-" + String.format("%04d", contadorExpedientes++);
        Adopcion nueva = new Adopcion(id, idMascota, dniAdoptante);
        listaAdopciones.add(nueva);
        mascotaController.actualizarEstado(idMascota, "En proceso de adopción");
        System.out.println("✔ Solicitud generada: " + id);
        return nueva;
    }

    public boolean concretarAdopcion(String idAdopcion, LocalDate fechaEntrega) {
        Adopcion a = buscarPorId(idAdopcion);
        if (a == null || !a.getEstado().equals("En proceso")) {
            System.out.println("❌ Expediente no válido.");
            return false;
        }
        a.aprobarAdopcion(fechaEntrega);
        mascotaController.actualizarEstado(a.getIdMascota(), "Adoptada");
        System.out.println("✔ Adopción concretada.");
        return true;
    }

    public boolean procesarDevolucionMascota(String idAdopcion, int motivoCodigo, String detalle) {
        Adopcion a = buscarPorId(idAdopcion);
        if (a == null || !a.getEstado().equals("Concretada")) {
            System.out.println("❌ No se puede devolver.");
            return false;
        }
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
        String motivo = (motivoCodigo >= 1 && motivoCodigo <= motivos.length) ? motivos[motivoCodigo-1] : "Motivo no especificado";
        a.revertirAdopcion(motivo, detalle);
        mascotaController.actualizarEstado(a.getIdMascota(), "Disponible");
        System.out.println("✔ Devolución procesada. Motivo: " + motivo);
        return true;
    }

    public boolean programarVisitaSeguimiento(String idAdopcion, LocalDate fechaVisita) {
        // Versión simple (sin hora) para compatibilidad con código antiguo
        return programarVisitaSeguimiento(idAdopcion, fechaVisita, LocalTime.of(10, 0)); // hora por defecto: 10:00
    }

    public boolean registrarObservacionesVisita(String idAdopcion, int numeroVisita, String observaciones) {
        Adopcion a = buscarPorId(idAdopcion);
        if (a == null) return false;
        VisitaSeguimiento v = (numeroVisita == 1) ? a.getVisita1() : (numeroVisita == 2) ? a.getVisita2() : null;
        if (v == null) return false;
        v.setObservaciones(observaciones);
        System.out.println("✔ Observaciones registradas para visita " + numeroVisita);
        return true;
    }

    // ============================================================
    // NUEVOS MÉTODOS (MEJORADOS)
    // ============================================================

    public boolean programarVisitaSeguimiento(String idAdopcion, LocalDate fechaVisita, LocalTime horaVisita) {
        Adopcion a = buscarPorId(idAdopcion);
        if (a == null || !a.getEstado().equals("Concretada")) {
            System.out.println("❌ No se puede programar visita.");
            return false;
        }
        VisitaSeguimiento v = new VisitaSeguimiento(fechaVisita, horaVisita);
        if (!a.asignarVisita(v)) {
            System.out.println("❌ Límite de 2 visitas alcanzado.");
            return false;
        }
        System.out.println("✔ Visita programada para " + fechaVisita + " a las " + horaVisita);
        return true;
    }

    public boolean procesarDevolucionDetallada(String idAdopcion, int motivoCodigo, String detalle,
                                               String motivoDetallado, String accion, boolean cuarentena) {
        Adopcion a = buscarPorId(idAdopcion);
        if (a == null || !a.getEstado().equals("Concretada")) {
            System.out.println("❌ No se puede devolver.");
            return false;
        }
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
        String motivo = (motivoCodigo >= 1 && motivoCodigo <= motivos.length) ? motivos[motivoCodigo-1] : "Motivo no especificado";
        a.revertirAdopcion(motivo, detalle);
        a.setMotivoDevolucionDetallado(motivoDetallado);
        a.setAccionTomada(accion);
        a.setCuarentena(cuarentena);
        mascotaController.actualizarEstado(a.getIdMascota(), "Disponible");
        if (cuarentena) {
            mascotaController.actualizarEstado(a.getIdMascota(), "En cuarentena");
            System.out.println("⚠️ Mascota puesta en cuarentena por: " + motivoDetallado);
        }
        System.out.println("✔ Devolución detallada procesada. Motivo: " + motivo);
        return true;
    }

    // ============================================================
    // MÉTODOS DE CONSULTA
    // ============================================================

    public Adopcion buscarPorId(String idAdopcion) {
        for (Adopcion a : listaAdopciones) {
            if (a.getIdAdopcion().equals(idAdopcion)) return a;
        }
        return null;
    }

    public List<Adopcion> obtenerTodas() {
        return listaAdopciones;
    }

    public List<Adopcion> obtenerPorEstado(String estado) {
        List<Adopcion> res = new ArrayList<>();
        for (Adopcion a : listaAdopciones) {
            if (a.getEstado().equalsIgnoreCase(estado)) res.add(a);
        }
        return res;
    }

    public List<Adopcion> obtenerDevoluciones() {
        List<Adopcion> res = new ArrayList<>();
        for (Adopcion a : listaAdopciones) {
            if (a.isEsDevuelta()) res.add(a);
        }
        return res;
    }

    public List<Adopcion> obtenerSolicitudesEnProceso() {
        List<Adopcion> res = new ArrayList<>();
        for (Adopcion a : listaAdopciones) {
            if (a.getEstado().equals("En proceso")) res.add(a);
        }
        return res;
    }

    public List<Adopcion> obtenerSolicitudesConcretadas() {
        List<Adopcion> res = new ArrayList<>();
        for (Adopcion a : listaAdopciones) {
            if (a.getEstado().equals("Concretada")) res.add(a);
        }
        return res;
    }

    // ============================================================
    // MÉTODOS PARA PERSISTENCIA
    // ============================================================

    public void setListaAdopciones(List<Adopcion> lista) {
        this.listaAdopciones = lista;
    }

    public List<Adopcion> getListaAdopciones() {
        return listaAdopciones;
    }
}