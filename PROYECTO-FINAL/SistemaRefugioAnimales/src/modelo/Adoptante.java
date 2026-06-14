package modelo;

public class Adoptante {
	
    // Atributos base definidos
    private String dni;                // Identificador único (Clave primaria lógica)
    private String nombre;             // Nombres completos del solicitante
    private String telefono;           // Número de contacto principal
    private String estadoValidacion;   // Estados: En evaluación, Aprobado, Rechazado

    // Atributos operativos de evaluación (RF23, RF25, RF26, RF27)
    private int edad;                  // Edad del solicitante
    private String direccion;          // Dirección domiciliaria actual
    private String preferencias;       // Ej: "Prefiere gato adulto", "Perro tamaño pequeño"
    private String tipoVivienda;       // Ej: Casa propia, Departamento, Alquilado
    
    // Cuadro de comentarios para auditoría de rechazos (RF29)
    private String motivoRechazo;      // Texto obligatorio si el estado cambia a Rechazado

    // Constructor vacío por defecto
    public Adoptante() {
        this.estadoValidacion = "En evaluación";
    }

    // Constructor principal para el registro inicial (RF23)
    public Adoptante(String dni, String nombre, int edad, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.estadoValidacion = "En evaluación"; // Estado inicial automático
    }

    // Getters y Setters 
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstadoValidacion() { return estadoValidacion; }
    
    // Método centralizado para actualizar estado y registrar el cuadro de comentarios simultáneamente
    
    public void setEstadoValidacion(String estadoValidacion, String motivoRechazo) { 
        this.estadoValidacion = estadoValidacion; 
        this.motivoRechazo = motivoRechazo;
    }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }

    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }

    public String getMotivoRechazo() { return motivoRechazo; }

    @Override
    public String toString() {
        return "Adoptante DNI: " + dni + " | Nombre: " + nombre + " | Tel: " + telefono + 
               " | Estado: " + estadoValidacion + 
               (motivoRechazo != null && !motivoRechazo.isEmpty() ? " | Observación: " + motivoRechazo : "");
    }
}
