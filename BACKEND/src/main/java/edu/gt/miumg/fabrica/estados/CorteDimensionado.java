package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class CorteDimensionado implements EstadoOp {
  @Override public String nombre() { return Estado.CORTE_DIMENSIONADO.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.SECADO_TRATAMIENTO);
  }
}
