package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class InspeccionCalidad implements EstadoOp {
  @Override public String nombre() { return Estado.INSPECCION_CALIDAD.name(); }

  @Override
  public void avanzar(ProcesoProduccion p) {
    throw new IllegalStateException("Desde INSPECCION_CALIDAD usa decidir(true|false) o reprocesar().");
  }

  @Override
  public void decidir(ProcesoProduccion p, boolean apta) {
    if (apta) p.setEstadoActual(Estado.CORTE_DIMENSIONADO);
    else      p.setEstadoActual(Estado.RECHAZADA);
  }

  public void reprocesar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.REPROCESO);
  }
}
