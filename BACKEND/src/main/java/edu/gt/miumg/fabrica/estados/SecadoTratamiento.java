package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class SecadoTratamiento implements EstadoOp {
  @Override public String nombre() { return Estado.SECADO_TRATAMIENTO.name(); }
  @Override public void avanzar(ProcesoProduccion p) {
    p.setEstadoActual(Estado.ALMACENADO);
  }
}
