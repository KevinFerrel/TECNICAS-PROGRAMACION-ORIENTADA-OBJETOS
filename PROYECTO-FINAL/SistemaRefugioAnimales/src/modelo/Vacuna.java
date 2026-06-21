package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Vacuna implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idVacuna;
    private String nombreVacuna;
    private LocalDate fechaAplicacion;
    private String idMascota;
    private String laboratorio;
    private LocalDate fechaProximaDosis;
    private boolean pendiente;

    public Vacuna() {
        this.pendiente = true;
    }

    public Vacuna(String idVacuna, String idMascota, String nombreVacuna, String laboratorio) {
        this.idVacuna = idVacuna;
        this.idMascota = idMascota;
        this.nombreVacuna = nombreVacuna;
        this.laboratorio = laboratorio;
        this.pendiente = true;
    }

    public void registrarAplicacion(LocalDate fecha) {
        this.fechaAplicacion = fecha;
        this.pendiente = false;
    }

    // Getters y Setters
    public String getIdVacuna() { return idVacuna; }
    public void setIdVacuna(String idVacuna) { this.idVacuna = idVacuna; }
    public String getNombreVacuna() { return nombreVacuna; }
    public void setNombreVacuna(String nombreVacuna) { this.nombreVacuna = nombreVacuna; }
    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDate fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public String getIdMascota() { return idMascota; }
    public void setIdMascota(String idMascota) { this.idMascota = idMascota; }
    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
    public LocalDate getFechaProximaDosis() { return fechaProximaDosis; }
    public void setFechaProximaDosis(LocalDate fechaProximaDosis) { this.fechaProximaDosis = fechaProximaDosis; }
    public boolean isPendiente() { return pendiente; }
    public void setPendiente(boolean pendiente) { this.pendiente = pendiente; }

    @Override
    public String toString() {
        return nombreVacuna + " | " + (pendiente ? "Pendiente" : "Aplicada el " + fechaAplicacion);
    }
}