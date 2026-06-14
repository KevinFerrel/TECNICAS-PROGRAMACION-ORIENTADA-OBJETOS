package modelo;

import java.time.LocalDate;

public class VisitaSeguimiento {
    private LocalDate fechaVisita;     // RF36: Fecha programada de la visita
    private String observaciones;      // RF37: Detalles recabados en el entorno

    public VisitaSeguimiento(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
        this.observaciones = "Pendiente de realizar";
    }

    public LocalDate getFechaVisita() { return fechaVisita; }
    public void setFechaVisita(LocalDate fechaVisita) { this.fechaVisita = fechaVisita; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "Fecha: " + fechaVisita + " | Obs: " + observaciones;
    }
}
