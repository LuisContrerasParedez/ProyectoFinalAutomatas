package edu.gt.miumg.fabrica.persistencia;

import java.time.OffsetDateTime;

import edu.gt.miumg.fabrica.MateriaPrima;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "materia_prima")
public class MateriaPrimaEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private MateriaPrima.Tipo tipo;

  @Enumerated(EnumType.STRING)
  private MateriaPrima.Calidad calidad;

  private double humedad_porc;
  private int cantidad;

  private OffsetDateTime created_at = OffsetDateTime.now();

  public Long getId() { return id; }
  public MateriaPrima.Tipo getTipo() { return tipo; }
  public MateriaPrima.Calidad getCalidad() { return calidad; }
  public double getHumedad_porc() { return humedad_porc; }
  public int getCantidad() { return cantidad; }

  public void setTipo(MateriaPrima.Tipo tipo) { this.tipo = tipo; }
  public void setCalidad(MateriaPrima.Calidad calidad) { this.calidad = calidad; }
  public void setHumedad_porc(double v) { this.humedad_porc = v; }
  public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
