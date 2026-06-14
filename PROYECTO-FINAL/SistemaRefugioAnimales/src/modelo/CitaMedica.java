package modelo;

import java.time.LocalDateTime;

public class CitaMedica {
    // Atributos definidos
    private String idCita;                 // Identificador correlativo de la cita
    private LocalDateTime fechaHora;       // RF11: Fecha y hora exacta de la cita veterinaria
    private String diagnostico;            // RF13: Diagnóstico clínico del profesional
    private double peso;                   // RF12: Peso del animal en kilogramos

    // Atributos de enlace y control (RF)
    private String idMascota;              // RF11: Código de la mascota vinculada a la cita
    private double talla;                  // RF12: Altura/talla del animal en centímetros
    private String estado;                 // RF14: Pendiente, Atendida, Cancelada, Reprogramada

    public CitaMedica() {
        this.estado = "Pendiente";
    }

    public CitaMedica(String idCita, String idMascota, LocalDateTime fechaHora) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.fechaHora = fechaHora;
        this.estado = "Pendiente";
    }

    // Getters y Setters para manipulación de datos
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
        return "Cita [" + idCita + "] Mascota: " + idMascota + " | Fecha/Hora: " + fechaHora + 
               " | Estado: " + estado + (diagnostico != null ? " | Diagnóstico: " + diagnostico : "");
    }
}
