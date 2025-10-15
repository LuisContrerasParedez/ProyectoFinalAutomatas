package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Almacenado implements EstadoOp {
  @Override public String nombre() { return Estado.ALMACENADO.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.DISTRIBUIDO_PRODUCCION);
  }
}
