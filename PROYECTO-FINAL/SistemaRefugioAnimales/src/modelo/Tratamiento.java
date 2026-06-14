package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tratamiento {
    // Atributos de correspondencia
    private String idTratamiento;          // Código identificador único del tratamiento
    private String descripcion;            // RF16 : Detalles clínicos del tratamiento
    private String estado;                 // RF17 : En curso, Suspendido, Finalizado

    // Atributos de negocio (RF)
    private String idMascota;                  // Código de la mascota que recibe el tratamiento
    private LocalDate fechaInicio;             // RF16: Fecha de inicio clínico
    private int duracionDias;                  // RF16: Período de duración prescrito en días
    private List<Medicina> medicinasAsociadas; // RF18: Medicamentos añadidos al tratamiento

    public Tratamiento() {
        this.medicinasAsociadas = new ArrayList<>();
        this.estado = "En curso";
    }

    public Tratamiento(String idTratamiento, String idMascota, String descripcion, int duracionDias) {
        this.idTratamiento = idTratamiento;
        this.idMascota = idMascota;
        this.descripcion = descripcion;
        this.duracionDias = duracionDias;
        this.fechaInicio = LocalDate.now();
        this.estado = "En curso";
        this.medicinasAsociadas = new ArrayList<>();
    }

    // Métodos operativos
    public void iniciarTratamiento() {
        this.estado = "En curso";
    }

    public void finalizarTratamiento() {
        this.estado = "Finalizado";
    }

    // RF18: Agregar elementos farmacológicos al listado
    public void agregarMedicina(Medicina med) {
        this.medicinasAsociadas.add(med);
    }

    // Getters y Setters
    public String getIdTratamiento() { return idTratamiento; }
    public void setIdTratamiento(String idTratamiento) { this.idTratamiento = idTratamiento; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getIdMascota() { return idMascota; }
    public void setIdMascota(String idMascota) { this.idMascota = idMascota; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }

    public List<Medicina> getMedicinasAsociadas() { return medicinasAsociadas; }

    @Override
    public String toString() {
        return "Tratamiento [" + idTratamiento + "] Mascota: " + idMascota + " | Estado: " + estado + 
               " | Duración: " + duracionDias + " días | Medicinas: " + medicinasAsociadas;
    }
}
