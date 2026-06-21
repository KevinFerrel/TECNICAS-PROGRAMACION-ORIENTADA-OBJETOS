package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tratamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idTratamiento;
    private String descripcion;
    private String estado;
    private String idMascota;
    private LocalDate fechaInicio;
    private int duracionDias;
    private List<Medicina> medicinasAsociadas;

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

    public void iniciarTratamiento() { this.estado = "En curso"; }
    public void finalizarTratamiento() { this.estado = "Finalizado"; }
    public void agregarMedicina(Medicina med) { this.medicinasAsociadas.add(med); }

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
        return "Tratamiento " + idTratamiento + " | " + estado;
    }
}