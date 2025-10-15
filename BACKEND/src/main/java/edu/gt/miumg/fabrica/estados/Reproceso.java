package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Reproceso implements EstadoOp {
  @Override public String nombre() { return Estado.REPROCESO.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.RECEPCION_MP);
  }
}
