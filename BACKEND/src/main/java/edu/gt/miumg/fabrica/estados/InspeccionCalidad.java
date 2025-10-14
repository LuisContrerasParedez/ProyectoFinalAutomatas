package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class InspeccionCalidad implements Estado {
    @Override public String obtenerNombre() { return "Inspeccion de Calidad"; }

    @Override
    public void avanzar(ProcesoProduccion proceso) {
        ProcesoProduccion.DecisionInspeccion d = proceso.getDecisionInspeccion();
        if (d == null) {
            throw new IllegalStateException(
                "Debe decidir en Inspeccion: APTA o RECHAZADA antes de avanzar."
            );
        }
        switch (d) {
            case APTA      -> proceso.transicionarA(new CorteDimensionado());
            case RECHAZADA -> proceso.transicionarA(new Rechazado());
        }
    }
}
