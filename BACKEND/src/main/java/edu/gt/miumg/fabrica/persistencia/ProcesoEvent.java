package edu.gt.miumg.fabrica.persistencia;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proceso_event")
public class ProcesoEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "proceso_id", nullable = false)
  private UUID procesoId;

  @Column(name = "estado_origen")
  private String estadoOrigen;

  @Column(name = "estado_destino")
  private String estadoDestino;

  @Column(name = "mp_tipo")
  private String mpTipo;

  @Column(name = "mp_calidad")
  private String mpCalidad;

  @Column(name = "mp_humedad")
  private Double mpHumedad;

  @Column(name = "mp_cantidad")
  private Integer mpCantidad;

  @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
  private OffsetDateTime createdAt = OffsetDateTime.now();

  public Long getId() { return id; }
  public UUID getProcesoId() { return procesoId; }
  public void setProcesoId(UUID procesoId) { this.procesoId = procesoId; }
  public String getEstadoOrigen() { return estadoOrigen; }
  public void setEstadoOrigen(String estadoOrigen) { this.estadoOrigen = estadoOrigen; }
  public String getEstadoDestino() { return estadoDestino; }
  public void setEstadoDestino(String estadoDestino) { this.estadoDestino = estadoDestino; }
  public String getMpTipo() { return mpTipo; }
  public void setMpTipo(String mpTipo) { this.mpTipo = mpTipo; }
  public String getMpCalidad() { return mpCalidad; }
  public void setMpCalidad(String mpCalidad) { this.mpCalidad = mpCalidad; }
  public Double getMpHumedad() { return mpHumedad; }
  public void setMpHumedad(Double mpHumedad) { this.mpHumedad = mpHumedad; }
  public Integer getMpCantidad() { return mpCantidad; }
  public void setMpCantidad(Integer mpCantidad) { this.mpCantidad = mpCantidad; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
