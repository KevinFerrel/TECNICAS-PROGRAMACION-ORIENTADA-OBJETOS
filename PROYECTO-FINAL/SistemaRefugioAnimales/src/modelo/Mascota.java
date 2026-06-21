package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Mascota implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idMascota;
    private String nombre;
    private String especie;
    private String estado;
    private String raza;
    private LocalDate fechaRescate;
    private LocalDate fechaIngreso;
    private String color;
    private String tamano;
    private String marcasParticulares;
    private String observacionesConducta;
    private boolean activo;
    private String rutaFoto;  // <-- NUEVO

    public Mascota() {
        this.fechaIngreso = LocalDate.now();
        this.estado = "Disponible";
        this.activo = true;
        this.rutaFoto = "sin_foto.jpg";
    }

    public Mascota(String idMascota, String nombre, String especie, String raza, LocalDate fechaRescate) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaRescate = fechaRescate;
        this.fechaIngreso = LocalDate.now();
        this.estado = "Disponible";
        this.activo = true;
        this.rutaFoto = "sin_foto.jpg";
    }

    // Getters y Setters (incluyendo rutaFoto)
    public String getIdMascota() { return idMascota; }
    public void setIdMascota(String idMascota) { this.idMascota = idMascota; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public LocalDate getFechaRescate() { return fechaRescate; }
    public void setFechaRescate(LocalDate fechaRescate) { this.fechaRescate = fechaRescate; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public String getMarcasParticulares() { return marcasParticulares; }
    public void setMarcasParticulares(String marcasParticulares) { this.marcasParticulares = marcasParticulares; }
    public String getObservacionesConducta() { return observacionesConducta; }
    public void setObservacionesConducta(String observacionesConducta) { this.observacionesConducta = observacionesConducta; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }

    @Override
    public String toString() {
        return "Mascota [" + idMascota + "] " + nombre + " | " + especie + " | " + estado + " | Foto: " + rutaFoto;
    }
}