package edu.gt.miumg.fabrica;

import edu.gt.miumg.fabrica.estados.EstadoFactory;
import edu.gt.miumg.fabrica.estados.EstadoOp;

/**
 * Modelo de dominio del proceso de producción.
 * Mantiene el estado actual y la materia prima asociada.
 * Las transiciones se delegan a las clases de la carpeta "estados".
 */
public class ProcesoProduccion {

    /** Estado actual del proceso */
    private Estado estadoActual;   // Al crear debería ser RECEPCION_MP
    /** Indicador de estado terminal (DISTRIBUIDO_PRODUCCION o RECHAZADA, o el que marque el estado) */
    private boolean terminal;
    /** Snapshot de la materia prima asociada */
    private MateriaPrima materiaPrima;

    public ProcesoProduccion() { }

    public ProcesoProduccion(Estado estadoInicial) {
        this.estadoActual = estadoInicial;
        recomputarTerminal();
    }

    // --- Transiciones delegadas (Patrón Estado) ---

    /** Avanza según la lógica del estado actual. Lanza IllegalStateException si no aplica. */
    public void avanzar() {
        if (estadoActual == null) {
            throw new IllegalStateException("Estado actual nulo: inicializa el proceso antes de avanzar.");
        }
        EstadoOp op = EstadoFactory.of(estadoActual);
        op.avanzar(this);          // la implementación de estado hará setEstadoActual(...)
        recomputarTerminal();      // recalcula el flag terminal después del cambio
    }

    /**
     * Decide en estados que lo permitan (p.ej. INSPECCION_CALIDAD).
     * @param apta true para pasar a CORTE_DIMENSIONADO; false para RECHAZADA
     */
    public void decidir(boolean apta) {
        if (estadoActual == null) {
            throw new IllegalStateException("Estado actual nulo: inicializa el proceso antes de decidir.");
        }
        EstadoOp op = EstadoFactory.of(estadoActual);
        op.decidir(this, apta);    // la implementación de estado hará setEstadoActual(...)
        recomputarTerminal();      // recalcula el flag terminal después del cambio
    }

    /** Recalcula la marca terminal en base al estado actual y su implementación. */
    private void recomputarTerminal() {
        if (estadoActual == null) {
            this.terminal = false;
            return;
        }
        // Terminal por definición del flujo + por lo que reporte la implementación del estado
        boolean terminalPorEnum =
                estadoActual == Estado.DISTRIBUIDO_PRODUCCION ||
                estadoActual == Estado.RECHAZADA;
        boolean terminalPorEstado = EstadoFactory.of(estadoActual).terminal();
        this.terminal = terminalPorEnum || terminalPorEstado;
    }

    // --- Getters / Setters ---

    public Estado getEstadoActual() { return estadoActual; }

    /** Ajusta el estado actual (lo usan las clases de la carpeta "estados"). */
    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
        recomputarTerminal();
    }

    public boolean isTerminal() { return terminal; }

    /** Permite forzar la marca terminal (normalmente NO necesitas llamarlo manualmente). */
    public void setTerminal(boolean terminal) { this.terminal = terminal; }

    public MateriaPrima getMateriaPrima() { return materiaPrima; }

    public void setMateriaPrima(MateriaPrima materiaPrima) { this.materiaPrima = materiaPrima; }

    @Override
    public String toString() {
        return "ProcesoProduccion{" +
                "estadoActual=" + estadoActual +
                ", terminal=" + terminal +
                ", materiaPrima=" + materiaPrima +
                '}';
    }
}
