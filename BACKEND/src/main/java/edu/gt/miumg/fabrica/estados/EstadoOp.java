package edu.gt.miumg.fabrica.estados;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public interface EstadoOp {
  String nombre();
  void avanzar(ProcesoProduccion p);
  default void decidir(ProcesoProduccion p, boolean apta) {
    throw new IllegalStateException("Decidir no aplica en " + nombre());
  }

  default boolean terminal() { return false; }
}
