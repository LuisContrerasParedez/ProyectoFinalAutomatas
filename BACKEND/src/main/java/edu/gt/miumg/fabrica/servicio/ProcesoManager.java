package edu.gt.miumg.fabrica.servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.gt.miumg.fabrica.MateriaPrima;
import edu.gt.miumg.fabrica.ProcesoProduccion;

@Service
public class ProcesoManager {
    private final Map<String, ProcesoProduccion> procesos = new HashMap<>();

    public String crearProceso() {
        String id = UUID.randomUUID().toString();
        procesos.put(id, new ProcesoProduccion());
        return id;
    }

    public List<String> listarProcesos() {
        return new ArrayList<>(procesos.keySet());
    }

    public ProcesoProduccion getProceso(String id) {
        ProcesoProduccion p = procesos.get(id);
        if (p == null) throw new IllegalArgumentException("Proceso no encontrado: " + id);
        return p;
    }

    public void setMateriaPrima(String id, MateriaPrima mp) {
        getProceso(id).setMateriaPrima(mp);
    }

    public void setDecision(String id, String decision) {
        ProcesoProduccion p = getProceso(id);
        try {
            ProcesoProduccion.DecisionInspeccion d =
                    ProcesoProduccion.DecisionInspeccion.valueOf(decision.toUpperCase());
            p.setDecisionInspeccion(d);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Decisión inválida: " + decision);
        }
    }

    public void avanzar(String id)    { getProceso(id).avanzar(); }
    public void reiniciar(String id)  { getProceso(id).reiniciar(); }
    public List<String> historial(String id) { return getProceso(id).historial(); }
}
