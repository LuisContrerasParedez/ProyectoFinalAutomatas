package edu.gt.miumg.fabrica.persistencia;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "proceso_event")
public class ProcesoEvent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String proceso_id;      
  private String estado_origen;
  private String estado_destino;

  private String mp_tipo;
  private String mp_calidad;
  private Double mp_humedad;
  private Integer mp_cantidad;

  private OffsetDateTime created_at = OffsetDateTime.now();

  // getters/setters
  public Long getId() { return id; }
  public String getProceso_id() { return proceso_id; }
  public void setProceso_id(String v) { this.proceso_id = v; }
  public String getEstado_origen() { return estado_origen; }
  public void setEstado_origen(String v) { this.estado_origen = v; }
  public String getEstado_destino() { return estado_destino; }
  public void setEstado_destino(String v) { this.estado_destino = v; }
  public String getMp_tipo() { return mp_tipo; }
  public void setMp_tipo(String v) { this.mp_tipo = v; }
  public String getMp_calidad() { return mp_calidad; }
  public void setMp_calidad(String v) { this.mp_calidad = v; }
  public Double getMp_humedad() { return mp_humedad; }
  public void setMp_humedad(Double v) { this.mp_humedad = v; }
  public Integer getMp_cantidad() { return mp_cantidad; }
  public void setMp_cantidad(Integer v) { this.mp_cantidad = v; }
}
