package edu.gt.miumg.fabrica;

public class ProcesoProduccion {

    private Estado estadoActual;   // null al crear, o RECEPCION_MP
    private boolean terminal;      // true cuando llega a DISTRIBUIDO_PRODUCCION o RECHAZADA
    private MateriaPrima materiaPrima;

    public ProcesoProduccion() {}

    // --- Estado ---
    public Estado getEstadoActual() {
        return estadoActual;
    }
    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    // --- Terminal ---
    public boolean isTerminal() {
        return terminal;
    }
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    // --- Materia prima ---
    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }
    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }
}
