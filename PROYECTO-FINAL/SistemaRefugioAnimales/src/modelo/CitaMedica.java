package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CitaMedica implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idCita;
    private LocalDateTime fechaHora;
    private String diagnostico;
    private double peso;
    private String idMascota;
    private double talla;
    private String estado;

    public CitaMedica() {
        this.estado = "Pendiente";
    }

    public CitaMedica(String idCita, String idMascota, LocalDateTime fechaHora) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.fechaHora = fechaHora;
        this.estado = "Pendiente";
    }

    // Getters y Setters
    public String getIdCita() { return idCita; }
    public void setIdCita(String idCita) { this.idCita = idCita; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getIdMascota() { return idMascota; }
    public void setIdMascota(String idMascota) { this.idMascota = idMascota; }
    public double getTalla() { return talla; }
    public void setTalla(double talla) { this.talla = talla; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Cita " + idCita + " | " + fechaHora + " | " + estado;
    }
}