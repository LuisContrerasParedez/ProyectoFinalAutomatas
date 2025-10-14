package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class CorteDimensionado implements Estado {
    @Override public String obtenerNombre() { return "Corte y Dimensionado"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        proceso.transicionarA(new SecadoTratamiento());
    }
}
