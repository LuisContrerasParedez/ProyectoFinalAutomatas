package edu.gt.miumg.fabrica.persistencia;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "procesos")
public class Proceso {
    @Id @GeneratedValue
    private UUID id;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "estado_actual", nullable = false)
    private String estadoActual;

    @Column(name = "terminal", nullable = false)
    private boolean terminal;

    // Snapshot de MP
    @Column(name = "mp_tipo")     private String mpTipo;
    @Column(name = "mp_calidad")  private String mpCalidad;
    @Column(name = "mp_humedad")  private Double mpHumedad;
    @Column(name = "mp_cantidad") private Integer mpCantidad;

    // getters/setters
    public UUID getId() { return id; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String s) { this.estadoActual = s; }
    public boolean isTerminal() { return terminal; }
    public void setTerminal(boolean t) { this.terminal = t; }
    public String getMpTipo() { return mpTipo; }
    public void setMpTipo(String mpTipo) { this.mpTipo = mpTipo; }
    public String getMpCalidad() { return mpCalidad; }
    public void setMpCalidad(String mpCalidad) { this.mpCalidad = mpCalidad; }
    public Double getMpHumedad() { return mpHumedad; }
    public void setMpHumedad(Double mpHumedad) { this.mpHumedad = mpHumedad; }
    public Integer getMpCantidad() { return mpCantidad; }
    public void setMpCantidad(Integer mpCantidad) { this.mpCantidad = mpCantidad; }
}
