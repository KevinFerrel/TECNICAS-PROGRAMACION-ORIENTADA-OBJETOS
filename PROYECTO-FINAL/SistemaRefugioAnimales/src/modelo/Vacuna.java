package modelo;

import java.time.LocalDate;

public class Vacuna {
	
    // Atributos definidos 
    private String idVacuna;               // Identificador de control de la vacuna
    private String nombreVacuna;           // RF19 & UML: Nombre del componente biológico (ejem. Triple Felina)
    private LocalDate fechaAplicacion;     // RF19 & UML: Fecha efectiva de inoculación

    // Atributos complementarios (RF)
    private String idMascota;              // Código de la mascota a la que pertenece la vacuna
    private String laboratorio;            // RF19: Laboratorio fabricante
    private LocalDate fechaProximaDosis;   // RF20: Fecha programada para el refuerzo
    private boolean pendiente;             // RF21: Estado de aplicación (true = requiere programarse)

    public Vacuna() {
        this.pendiente = true;
    }

    public Vacuna(String idVacuna, String idMascota, String nombreVacuna, String laboratorio) {
        this.idVacuna = idVacuna;
        this.idMascota = idMascota;
        this.nombreVacuna = nombreVacuna;
        this.laboratorio = laboratorio;
        this.pendiente = true; // Inicia registrada como planificada o por aplicar
    }

    // Método de ejecución 
    public void registrarAplicacion(LocalDate fecha) {
        this.fechaAplicacion = fecha;
        this.pendiente = false; // Deja de estar en la cola de pendientes inmediatas
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
        return "Vacuna: " + nombreVacuna + " | Laboratorio: " + laboratorio + 
               (pendiente ? " | [PENDIENTE - Próxima: " + fechaProximaDosis + "]" : " | Aplicada el: " + fechaAplicacion);
    }
}
