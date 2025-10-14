package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class DistribuidoProduccion implements Estado {
    @Override public String obtenerNombre() { return "Distribuido a Produccion"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        throw new IllegalStateException("Estado final alcanzado: no se puede avanzar mas.");
    }
}
