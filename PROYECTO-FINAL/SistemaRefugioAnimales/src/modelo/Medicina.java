package modelo;

import java.io.Serializable;

public class Medicina implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String dosis;

    public Medicina() {}

    public Medicina(String nombre, String dosis) {
        this.nombre = nombre;
        this.dosis = dosis;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    @Override
    public String toString() {
        return nombre + " (" + dosis + ")";
    }
}