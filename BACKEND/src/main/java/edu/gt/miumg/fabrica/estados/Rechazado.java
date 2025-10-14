package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Rechazado implements Estado {
    @Override public String obtenerNombre() { return "Materia Prima Rechazada"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        throw new IllegalStateException("Flujo terminado en estado Rechazado.");
    }
}
