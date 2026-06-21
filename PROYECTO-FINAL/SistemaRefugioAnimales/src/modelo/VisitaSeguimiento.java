package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class VisitaSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate fechaVisita;
    private LocalTime horaVisita;
    private String observaciones;
    private boolean notificado;

    public VisitaSeguimiento(LocalDate fechaVisita, LocalTime horaVisita) {
        this.fechaVisita = fechaVisita;
        this.horaVisita = horaVisita;
        this.observaciones = "Pendiente de realizar";
        this.notificado = false;
    }

    // Getters y Setters
    public LocalDate getFechaVisita() { return fechaVisita; }
    public void setFechaVisita(LocalDate fechaVisita) { this.fechaVisita = fechaVisita; }
    public LocalTime getHoraVisita() { return horaVisita; }
    public void setHoraVisita(LocalTime horaVisita) { this.horaVisita = horaVisita; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public boolean isNotificado() { return notificado; }
    public void setNotificado(boolean notificado) { this.notificado = notificado; }

    @Override
    public String toString() {
        return "Visita: " + fechaVisita + " a las " + horaVisita + " - " + observaciones;
    }
}