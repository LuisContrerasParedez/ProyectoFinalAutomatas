package edu.gt.miumg.fabrica.estados;

import java.util.Map;
import java.util.function.Supplier;

import edu.gt.miumg.fabrica.Estado;

public final class EstadoFactory {
  private EstadoFactory() {}

  private static final Map<Estado, Supplier<EstadoOp>> MAP = Map.of(
    Estado.RECEPCION_MP,        Recepcion::new,
    Estado.INSPECCION_CALIDAD,  InspeccionCalidad::new,
    Estado.CORTE_DIMENSIONADO,  CorteDimensionado::new,
    Estado.SECADO_TRATAMIENTO,  SecadoTratamiento::new,
    Estado.ALMACENADO,          Almacenado::new,
    Estado.DISTRIBUIDO_PRODUCCION, DistribuidoProduccion::new,
    Estado.RECHAZADA,           Rechazada::new,
    Estado.REPROCESO,           Reproceso::new
  );

  public static EstadoOp of(Estado e) {
    var s = MAP.get(e);
    if (s == null) throw new IllegalArgumentException("Estado no soportado: " + e);
    return s.get();
  }
}
