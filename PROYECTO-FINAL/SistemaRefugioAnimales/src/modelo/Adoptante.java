package modelo;

import java.io.Serializable;

public class Adoptante implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dni;
    private String nombre;
    private int edad;
    private String telefono;
    private String email;          // NUEVO: correo electrónico
    private String direccion;
    private String preferencias;
    private String tipoVivienda;
    private String estadoValidacion;
    private String motivoRechazo;

    public Adoptante() {
        this.estadoValidacion = "En evaluación";
    }

    public Adoptante(String dni, String nombre, int edad, String telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
        this.estadoValidacion = "En evaluación";
    }

    // Getters y Setters
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }
    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }
    public String getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(String estadoValidacion, String motivoRechazo) {
        this.estadoValidacion = estadoValidacion;
        this.motivoRechazo = motivoRechazo;
    }
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    @Override
    public String toString() {
        String base = "Adoptante " + dni + " - " + nombre + " (" + estadoValidacion + ")";
        if (motivoRechazo != null && !motivoRechazo.isEmpty()) {
            base += " | Motivo: " + motivoRechazo;
        }
        return base;
    }
}