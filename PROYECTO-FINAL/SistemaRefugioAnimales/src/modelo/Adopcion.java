package modelo;

import java.time.LocalDate;

public class Adopcion {
    // Identificadores y relación (RF)
    private String idAdopcion;         // Código correlativo del expediente de adopción
    private String idMascota;          // Código del animal vinculado
    private String dniAdoptante;       // Documento del usuario responsable
    
    // Fechas operativas
    private LocalDate fechaSolicitud;  // Fecha de creación del trámite
    private LocalDate fechaContrato;   // Fecha oficial de aprobación del contrato legal
    private LocalDate fechaEntrega;    // RF34: Día en que el animal es retirado del refugio
    
    // Estado del trámite
    private String estado;             // En proceso, Concretada, Revertida

    // Auditoría Post-Adopción (RF36)
    private VisitaSeguimiento visita1;
    private VisitaSeguimiento visita2;

    // Variables de Control para Devoluciones y Reversiones (Restricción adicional)
    private boolean esDevuelta;
    private String motivoDevolucion;   // Almacena el inciso exacto de devolución
    private String detalleDevolucion;  // Cuadro de texto libre para ampliación del caso

    // Constructor principal
    public Adopcion(String idAdopcion, String idMascota, String dniAdoptante) {
        this.idAdopcion = idAdopcion;
        this.idMascota = idMascota;
        this.dniAdoptante = dniAdoptante;
        this.fechaSolicitud = LocalDate.now();
        this.estado = "En proceso";    // Estado inicial de la solicitud
        this.esDevuelta = false;
    }

    // Métodos exigidos para el RF
    public void aprobarAdopcion(LocalDate fechaEntrega) {
        this.estado = "Concretada";
        this.fechaContrato = LocalDate.now();
        this.fechaEntrega = fechaEntrega;
    }

    public void revertirAdopcion(String motivo, String detalleAdicional) {
        this.estado = "Revertida";
        this.esDevuelta = true;
        this.motivoDevolucion = motivo;
        this.detalleDevolucion = detalleAdicional;
    }

    // Asignación de visitas de seguimiento
    public boolean asignarVisita(VisitaSeguimiento visita) {
        if (this.visita1 == null) {
            this.visita1 = visita;
            return true;
        } else if (this.visita2 == null) {
            this.visita2 = visita;
            return true;
        }
        return false; // Se excedió el límite de 2 visitas permitidas
    }

    // Getters y Setters 
    public String getIdAdopcion() { return idAdopcion; }
    public void setIdAdopcion(String idAdopcion) { this.idAdopcion = idAdopcion; }

    public String getIdMascota() { return idMascota; }
    public void setIdMascota(String idMascota) { this.idMascota = idMascota; }

    public String getDniAdoptante() { return dniAdoptante; }
    public void setDniAdoptante(String dniAdoptante) { this.dniAdoptante = dniAdoptante; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public LocalDate getFechaContrato() { return fechaContrato; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isEsDevuelta() { return esDevuelta; }
    public String getMotivoDevolucion() { return motivoDevolucion; }
    public String getDetalleDevolucion() { return detalleDevolucion; }

    public VisitaSeguimiento getVisita1() { return visita1; }
    public VisitaSeguimiento getVisita2() { return visita2; }

    @Override
    public String toString() {
        return "Expediente [" + idAdopcion + "] | DNI: " + dniAdoptante + " | Mascota: " + idMascota + 
               " | Estado: " + estado + (esDevuelta ? " (DEVUELTA)" : "");
    }
}
