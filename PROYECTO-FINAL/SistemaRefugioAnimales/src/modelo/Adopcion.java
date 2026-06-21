package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Adopcion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idAdopcion;
    private String idMascota;
    private String dniAdoptante;
    private LocalDate fechaSolicitud;
    private LocalDate fechaContrato;
    private LocalDate fechaEntrega;
    private String estado;
    private VisitaSeguimiento visita1;
    private VisitaSeguimiento visita2;
    private boolean esDevuelta;
    private String motivoDevolucion;
    private String detalleDevolucion;
    private String motivoDevolucionDetallado;
    private String accionTomada;
    private boolean cuarentena;

    public Adopcion(String idAdopcion, String idMascota, String dniAdoptante) {
        this.idAdopcion = idAdopcion;
        this.idMascota = idMascota;
        this.dniAdoptante = dniAdoptante;
        this.fechaSolicitud = LocalDate.now();
        this.estado = "En proceso";
        this.esDevuelta = false;
        this.cuarentena = false;
    }

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

    public boolean asignarVisita(VisitaSeguimiento visita) {
        if (this.visita1 == null) {
            this.visita1 = visita;
            return true;
        } else if (this.visita2 == null) {
            this.visita2 = visita;
            return true;
        }
        return false;
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
    public String getMotivoDevolucionDetallado() { return motivoDevolucionDetallado; }
    public void setMotivoDevolucionDetallado(String motivoDevolucionDetallado) { this.motivoDevolucionDetallado = motivoDevolucionDetallado; }
    public String getAccionTomada() { return accionTomada; }
    public void setAccionTomada(String accionTomada) { this.accionTomada = accionTomada; }
    public boolean isCuarentena() { return cuarentena; }
    public void setCuarentena(boolean cuarentena) { this.cuarentena = cuarentena; }

    @Override
    public String toString() {
        return "Expediente " + idAdopcion + " | " + estado + (esDevuelta ? " (DEVUELTA)" : "");
    }
}