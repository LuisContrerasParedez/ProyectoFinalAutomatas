package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Rechazada implements EstadoOp {
  @Override public String nombre() { return Estado.RECHAZADA.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    throw new IllegalStateException("Estado terminal.");
  }
  @Override public boolean terminal() { return true; }
}
