package edu.gt.miumg.fabrica.estados;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.ProcesoProduccion;

public class Recepcion implements Estado {
    @Override public String obtenerNombre() { return "Recepcion de Materia Prima"; }
    @Override public void avanzar(ProcesoProduccion proceso) {
        proceso.transicionarA(new InspeccionCalidad());
    }
}
