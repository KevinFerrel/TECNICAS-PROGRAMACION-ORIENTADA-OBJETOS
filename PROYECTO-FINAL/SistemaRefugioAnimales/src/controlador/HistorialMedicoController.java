package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.CitaMedica;
import modelo.Tratamiento;
import modelo.Vacuna;
import modelo.Medicina;

public class HistorialMedicoController {
    private List<CitaMedica> listadoCitas = new ArrayList<>();
    private List<Tratamiento> listadoTratamientos = new ArrayList<>();
    private List<Vacuna> listadoVacunas = new ArrayList<>();

    // Contadores correlativos para autogenerar IDs textuales estables
    private int controlCitas = 1;
    private int controlTratamientos = 1;
    private int controlVacunas = 1;

    // RF11 & UML: programarCita() inicializa una nueva atención médica
    public CitaMedica programarCita(String idMascota, LocalDateTime fechaHora) {
        String idGenerado = "CIT-" + (controlCitas++);
        CitaMedica nuevaCita = new CitaMedica(idGenerado, idMascota, fechaHora);
        listadoCitas.add(nuevaCita);
        return nuevaCita;
    }

    // RF12: Registrar la evaluación morfométrica del animal
    public boolean registrarPesoTalla(String idCita, double peso, double talla) {
        CitaMedica cita = buscarCitaPorId(idCita);
        if (cita == null) return false;
        
        cita.setPeso(peso);
        cita.setTalla(talla);
        return true;
    }

    // RF13: Cargar el veredicto médico veterinario y pasar la cita a 'Atendida'
    public boolean registrarDiagnostico(String idCita, String diagnostico) {
        CitaMedica cita = buscarCitaPorId(idCita);
        if (cita == null) return false;
        
        cita.setDiagnostico(diagnostico);
        cita.setEstado("Atendida");
        return true;
    }

    // RF14 & UML: cancelarCita() cambia el estado operativo de una cita pendiente
    public boolean cancelarCita(String idCita) {
        CitaMedica cita = buscarCitaPorId(idCita);
        if (cita == null || !cita.getEstado().equals("Pendiente")) return false;
        
        cita.setEstado("Cancelada");
        return true;
    }

    // RF14: Modificar la planificación temporal de la cita veterinaria
    public boolean reprogramarCita(String idCita, LocalDateTime nuevaFechaHora) {
        CitaMedica cita = buscarCitaPorId(idCita);
        if (cita == null || !cita.getEstado().equals("Pendiente")) return false;
        
        cita.setFechaHora(nuevaFechaHora);
        cita.setEstado("Reprogramada");
        return true;
    }

    // HU03 / RF15: Devolver el listado de citas ordenadas por fecha cronológica
    public List<CitaMedica> obtenerHistorialCitas(String idMascota) {
        List<CitaMedica> filtradas = new ArrayList<>();
        for (CitaMedica c : listadoCitas) {
            if (c.getIdMascota().equalsIgnoreCase(idMascota)) {
                filtradas.add(c);
            }
        }
        // Ordenamiento burbuja o lambda básico para asegurar el orden cronológico exigido
        filtradas.sort((c1, c2) -> c1.getFechaHora().compareTo(c2.getFechaHora()));
        return filtradas;
    }

    // HU05 / RF16: Instanciar un esquema de tratamiento farmacológico
    public Tratamiento iniciarTratamientoMedico(String idMascota, String descripcion, int duracionDias) {
        String idGenerado = "TRAT-" + (controlTratamientos++);
        Tratamiento nuevoTratamiento = new Tratamiento(idGenerado, idMascota, descripcion, duracionDias);
        listadoTratamientos.add(nuevoTratamiento);
        return nuevoTratamiento;
    }

    // RF17: Modificar el estado del proceso curativo activo
    public boolean actualizarEstadoTratamiento(String idTratamiento, String nuevoEstado) {
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdTratamiento().equalsIgnoreCase(idTratamiento)) {
                t.setEstado(nuevoEstado);
                return true;
            }
        }
        return false;
    }

    // HU05 / RF18: Vincular una nueva medicina al tratamiento correspondiente
    public boolean asociarMedicinaATratamiento(String idTratamiento, String nombreMed, String dosis) {
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdTratamiento().equalsIgnoreCase(idTratamiento)) {
                t.agregarMedicina(new Medicina(nombreMed, dosis));
                return true;
            }
        }
        return false;
    }

    // HU03 / RF19: Registrar la inoculación biológica de una vacuna
    public Vacuna registrarAplicacionVacuna(String idMascota, String nombreVacuna, String laboratorio, LocalDate fecha) {
        String idGenerado = "VAC-" + (controlVacunas++);
        Vacuna nuevaVacuna = new Vacuna(idGenerado, idMascota, nombreVacuna, laboratorio);
        nuevaVacuna.registrarAplicacion(fecha);
        listadoVacunas.add(nuevaVacuna);
        return nuevaVacuna;
    }

    // HU04 / RF20: Dejar programado en el sistema un refuerzo o dosis futura
    public boolean programarProximaDosisVacuna(String idMascota, String nombreVacuna, String laboratorio, LocalDate fechaProxima) {
        String idGenerado = "VAC-" + (controlVacunas++);
        Vacuna vacunaAgendada = new Vacuna(idGenerado, idMascota, nombreVacuna, laboratorio);
        vacunaAgendada.setFechaProximaDosis(fechaProxima);
        vacunaAgendada.setPendiente(true);
        listadoVacunas.add(vacunaAgendada);
        return true;
    }

    // HU04 / RF21: Listar exclusivamente las vacunas que están pendientes de aplicación
    public List<Vacuna> obtenerVacunasPendientes(String idMascota) {
        List<Vacuna> pendientes = new ArrayList<>();
        for (Vacuna v : listadoVacunas) {
            if (v.getIdMascota().equalsIgnoreCase(idMascota) && v.isPendiente()) {
                pendientes.add(v);
            }
        }
        return pendientes;
    }

    // RF22: Generar y retornar el resumen textual consolidado del historial clínico
    public String generarResumenClinicoConsolidado(String idMascota) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("   RESUMEN CONSOLIDADO HISTORIAL CLÍNICO: ").append(idMascota).append("\n");
        sb.append("====================================================\n\n");

        sb.append(">> CRONOLOGÍA DE CITAS MÉDICAS:\n");
        List<CitaMedica> citas = obtenerHistorialCitas(idMascota);
        if (citas.isEmpty()) sb.append("   (Sin registros de citas médicas)\n");
        for (CitaMedica c : citas) sb.append("   - ").append(c.toString()).append("\n");

        sb.append("\n>> TRATAMIENTOS MÉDICOS ASIGNADOS:\n");
        boolean tieneTratamiento = false;
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdMascota().equalsIgnoreCase(idMascota)) {
                sb.append("   - ").append(t.toString()).append("\n");
                tieneTratamiento = true;
            }
        }
        if (!tieneTratamiento) sb.append("   (Sin tratamientos asignados)\n");

        sb.append("\n>> HISTORIAL DE VACUNACIÓN:\n");
        boolean tieneVacunas = false;
        for (Vacuna v : listadoVacunas) {
            if (v.getIdMascota().equalsIgnoreCase(idMascota)) {
                sb.append("   - ").append(v.toString()).append("\n");
                tieneVacunas = true;
            }
        }
        if (!tieneVacunas) sb.append("   (Sin vacunas registradas)\n");
        
        return sb.toString();
    }

    // Métodos utilitarios internos de búsqueda
    private CitaMedica buscarCitaPorId(String idCita) {
        for (CitaMedica c : listadoCitas) {
            if (c.getIdCita().equalsIgnoreCase(idCita)) return c;
        }
        return null;
    }
}
