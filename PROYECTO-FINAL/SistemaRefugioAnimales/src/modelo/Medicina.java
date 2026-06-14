package modelo;

public class Medicina {
    private String nombre; // Nombre comercial o genérico del medicamento
    private String dosis;  // Indicación de dosificación (ejem. 5ml cada 12 horas)

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
