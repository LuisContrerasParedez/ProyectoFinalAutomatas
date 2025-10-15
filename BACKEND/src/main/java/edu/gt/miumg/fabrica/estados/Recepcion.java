package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Recepcion implements EstadoOp {
  @Override public String nombre() { return Estado.RECEPCION_MP.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.INSPECCION_CALIDAD);
  }
}
