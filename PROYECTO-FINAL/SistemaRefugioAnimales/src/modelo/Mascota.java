package modelo;
import java.time.LocalDate;
public class Mascota {
    // Atributos definidos 
    private String idMascota;           // RF01: Identificador único de la mascota
    private String nombre;              // RF01: Nombre de la mascota
    private String especie;             // RF01: Especie (Perro, Gato, etc.)
    private String estado;              // RF03: Disponible, En cuarentena, En tratamiento, Adoptada

    // Atributos adicionales requeridos por las Reglas de Negocio (RF)
    private String raza;                  // RF01: Raza del animal
    private LocalDate fechaRescate;       // RF01: Fecha en la que fue rescatado
    private LocalDate fechaIngreso;       // RF10: Fecha automática de registro en el sistema
    private String color;                 // RF08: Descripción física - Color
    private String tamano;                // RF08: Descripción física - Tamaño
    private String marcasParticulares;    // RF08: Descripción física - Marcas distintivas
    private String observacionesConducta; // RF09: Problemas de comportamiento detectados
    private boolean activo;               // RF04: Control para baja lógica (false si falleció)

    // Constructor por defecto
    public Mascota() {
        this.fechaIngreso = LocalDate.now(); // RF10: Registro automático de la fecha actual
        this.estado = "Disponible";          // Estado inicial por defecto
        this.activo = true;                  // El registro inicia activo
    }

    // Constructor completo para el registro inicial (HU01 / RF01)
    public Mascota(String idMascota, String nombre, String especie, String raza, LocalDate fechaRescate) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaRescate = fechaRescate;
        this.fechaIngreso = LocalDate.now(); // RF10: Asignación automática de fecha del sistema
        this.estado = "Disponible";
        this.activo = true;
    }

    // Métodos de Acceso (Getters y Setters)
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

    // Representación en cadena de texto de la mascota para listados en consola
    
    @Override
    public String toString() {
        return "Mascota [" + idMascota + "] Nombre: " + nombre + " | Especie: " + especie + 
               " | Raza: " + raza + " | Estado: " + estado + (activo ? "" : " (BAJA POR FALLECIÓ)");
    }
}