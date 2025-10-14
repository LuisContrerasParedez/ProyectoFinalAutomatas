package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Almacenado implements Estado {
    @Override public String obtenerNombre() { return "Almacenado en Inventario"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        proceso.transicionarA(new DistribuidoProduccion());
    }
}
