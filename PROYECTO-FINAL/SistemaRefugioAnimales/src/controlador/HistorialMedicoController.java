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
    private int controlCitas = 1;
    private int controlTratamientos = 1;
    private int controlVacunas = 1;

    public CitaMedica programarCita(String idMascota, LocalDateTime fechaHora) {
        String id = "CIT-" + (controlCitas++);
        CitaMedica c = new CitaMedica(id, idMascota, fechaHora);
        listadoCitas.add(c);
        return c;
    }

    public boolean registrarPesoTalla(String idCita, double peso, double talla) {
        CitaMedica c = buscarCitaPorId(idCita);
        if (c == null) return false;
        c.setPeso(peso);
        c.setTalla(talla);
        return true;
    }

    public boolean registrarDiagnostico(String idCita, String diagnostico) {
        CitaMedica c = buscarCitaPorId(idCita);
        if (c == null) return false;
        c.setDiagnostico(diagnostico);
        c.setEstado("Atendida");
        return true;
    }

    public boolean cancelarCita(String idCita) {
        CitaMedica c = buscarCitaPorId(idCita);
        if (c == null || !c.getEstado().equals("Pendiente")) return false;
        c.setEstado("Cancelada");
        return true;
    }

    public boolean reprogramarCita(String idCita, LocalDateTime nuevaFecha) {
        CitaMedica c = buscarCitaPorId(idCita);
        if (c == null || !c.getEstado().equals("Pendiente")) return false;
        c.setFechaHora(nuevaFecha);
        c.setEstado("Reprogramada");
        return true;
    }

    public List<CitaMedica> obtenerHistorialCitas(String idMascota) {
        List<CitaMedica> res = new ArrayList<>();
        for (CitaMedica c : listadoCitas) {
            if (c.getIdMascota().equalsIgnoreCase(idMascota)) res.add(c);
        }
        res.sort((c1, c2) -> c1.getFechaHora().compareTo(c2.getFechaHora()));
        return res;
    }

    public Tratamiento iniciarTratamientoMedico(String idMascota, String descripcion, int duracionDias) {
        String id = "TRAT-" + (controlTratamientos++);
        Tratamiento t = new Tratamiento(id, idMascota, descripcion, duracionDias);
        listadoTratamientos.add(t);
        return t;
    }

    public boolean actualizarEstadoTratamiento(String idTratamiento, String nuevoEstado) {
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdTratamiento().equalsIgnoreCase(idTratamiento)) {
                t.setEstado(nuevoEstado);
                return true;
            }
        }
        return false;
    }

    public boolean asociarMedicinaATratamiento(String idTratamiento, String nombreMed, String dosis) {
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdTratamiento().equalsIgnoreCase(idTratamiento)) {
                t.agregarMedicina(new Medicina(nombreMed, dosis));
                return true;
            }
        }
        return false;
    }

    public Vacuna registrarAplicacionVacuna(String idMascota, String nombreVacuna, String laboratorio, LocalDate fecha) {
        String id = "VAC-" + (controlVacunas++);
        Vacuna v = new Vacuna(id, idMascota, nombreVacuna, laboratorio);
        v.registrarAplicacion(fecha);
        listadoVacunas.add(v);
        return v;
    }

    public boolean programarProximaDosisVacuna(String idMascota, String nombreVacuna, String laboratorio, LocalDate fechaProxima) {
        String id = "VAC-" + (controlVacunas++);
        Vacuna v = new Vacuna(id, idMascota, nombreVacuna, laboratorio);
        v.setFechaProximaDosis(fechaProxima);
        v.setPendiente(true);
        listadoVacunas.add(v);
        return true;
    }

    public List<Vacuna> obtenerVacunasPendientes(String idMascota) {
        List<Vacuna> res = new ArrayList<>();
        for (Vacuna v : listadoVacunas) {
            if (v.getIdMascota().equalsIgnoreCase(idMascota) && v.isPendiente()) res.add(v);
        }
        return res;
    }

    public String generarResumenClinicoConsolidado(String idMascota) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN CLÍNICO DE ").append(idMascota).append(" ===\n");
        sb.append("Citas:\n");
        for (CitaMedica c : obtenerHistorialCitas(idMascota)) sb.append(" - ").append(c).append("\n");
        sb.append("Tratamientos:\n");
        for (Tratamiento t : listadoTratamientos) {
            if (t.getIdMascota().equalsIgnoreCase(idMascota)) sb.append(" - ").append(t).append("\n");
        }
        sb.append("Vacunas:\n");
        for (Vacuna v : listadoVacunas) {
            if (v.getIdMascota().equalsIgnoreCase(idMascota)) sb.append(" - ").append(v).append("\n");
        }
        return sb.toString();
    }

    private CitaMedica buscarCitaPorId(String idCita) {
        for (CitaMedica c : listadoCitas) {
            if (c.getIdCita().equalsIgnoreCase(idCita)) return c;
        }
        return null;
    }

    // Para persistencia
    public void setListadoCitas(List<CitaMedica> lista) { this.listadoCitas = lista; }
    public List<CitaMedica> getListadoCitas() { return listadoCitas; }
    public void setListadoTratamientos(List<Tratamiento> lista) { this.listadoTratamientos = lista; }
    public List<Tratamiento> getListadoTratamientos() { return listadoTratamientos; }
    public void setListadoVacunas(List<Vacuna> lista) { this.listadoVacunas = lista; }
    public List<Vacuna> getListadoVacunas() { return listadoVacunas; }
}