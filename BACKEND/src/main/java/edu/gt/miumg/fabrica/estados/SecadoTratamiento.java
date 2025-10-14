package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class SecadoTratamiento implements Estado {
    @Override public String obtenerNombre() { return "Secado y Tratamiento"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        proceso.transicionarA(new Almacenado());
    }
}
