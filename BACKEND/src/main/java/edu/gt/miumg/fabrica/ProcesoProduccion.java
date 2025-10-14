package edu.gt.miumg.fabrica;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gt.miumg.fabrica.estados.Almacenado;
import edu.gt.miumg.fabrica.estados.CorteDimensionado;
import edu.gt.miumg.fabrica.estados.DistribuidoProduccion;
import edu.gt.miumg.fabrica.estados.InspeccionCalidad;
import edu.gt.miumg.fabrica.estados.Recepcion;
import edu.gt.miumg.fabrica.estados.Rechazado;
import edu.gt.miumg.fabrica.estados.SecadoTratamiento;

public final class ProcesoProduccion {

    public enum DecisionInspeccion { APTA, RECHAZADA }

    private DecisionInspeccion decisionInspeccion;
    public void setDecisionInspeccion(DecisionInspeccion d) { this.decisionInspeccion = d; }
    public DecisionInspeccion getDecisionInspeccion() { return decisionInspeccion; }

    private MateriaPrima materiaPrima;
    public void setMateriaPrima(MateriaPrima mp) { this.materiaPrima = mp; }
    public MateriaPrima getMateriaPrima() { return materiaPrima; }

    private Estado estadoActual;
    private final List<String> historial = new ArrayList<>();

    private static final Map<Class<? extends Estado>, Set<Class<? extends Estado>>> TRANSICIONES;
    static {
        Map<Class<? extends Estado>, Set<Class<? extends Estado>>> m = new HashMap<>();
        m.put(Recepcion.class,         Set.of(InspeccionCalidad.class));
        m.put(InspeccionCalidad.class, Set.of(CorteDimensionado.class, Rechazado.class));
        m.put(CorteDimensionado.class, Set.of(SecadoTratamiento.class));
        m.put(SecadoTratamiento.class, Set.of(Almacenado.class));
        m.put(Almacenado.class,        Set.of(DistribuidoProduccion.class));
        TRANSICIONES = Collections.unmodifiableMap(m);
    }

    public ProcesoProduccion() {
        this.estadoActual = new Recepcion();
        historial.add(stamp("Inicio en: " + estadoActual.obtenerNombre()));
    }

    public ProcesoProduccion(Estado estadoInicial) {
        this.estadoActual = estadoInicial == null ? new Recepcion() : estadoInicial;
        historial.add(stamp("Inicio en: " + this.estadoActual.obtenerNombre()));
    }

    public String  estadoActual()    { return estadoActual.obtenerNombre(); }
    public Estado  estadoActualObj() { return estadoActual; }
    public boolean esTerminal() {
        return (estadoActual instanceof DistribuidoProduccion)
            || (estadoActual instanceof Rechazado);
    }

    public void avanzar() { estadoActual.avanzar(this); }

    public void transicionarA(Estado nuevo) {
        Class<? extends Estado> origen  = estadoActual.getClass();
        Class<? extends Estado> destino = nuevo.getClass();
        Set<Class<? extends Estado>> permitidos =
                TRANSICIONES.getOrDefault(origen, Collections.emptySet());

        if (!permitidos.contains(destino)) {
            throw new IllegalStateException(
                "Transicion invalida: " + origen.getSimpleName() + " → " + destino.getSimpleName()
            );
        }

        String extra = (materiaPrima != null) ? " | MP=" + materiaPrima : "";
        String msg   = "Transicion: " + estadoActual.obtenerNombre() + " → " + nuevo.obtenerNombre() + extra;

        this.estadoActual = nuevo;
        this.decisionInspeccion = null;
        historial.add(stamp(msg));
    }

    public List<String> historial() { return Collections.unmodifiableList(historial); }

    public void reiniciar() {
        this.estadoActual = new Recepcion();
        this.decisionInspeccion = null;
        historial.clear();
        historial.add(stamp("Reinicio en: " + estadoActual.obtenerNombre()));
    }

    private static String stamp(String msg) {
        return "[" + LocalDateTime.now() + "] " + msg;
    }

    public void guardarHistorial(String rutaArchivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
            for (String line : historial) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
