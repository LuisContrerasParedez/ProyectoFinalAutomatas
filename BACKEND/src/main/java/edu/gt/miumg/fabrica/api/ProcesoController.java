package edu.gt.miumg.fabrica.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.gt.miumg.fabrica.MateriaPrima;
import edu.gt.miumg.fabrica.ProcesoProduccion;
import edu.gt.miumg.fabrica.api.dto.DecisionReq;
import edu.gt.miumg.fabrica.api.dto.EstadoRes;
import edu.gt.miumg.fabrica.api.dto.MateriaPrimaReq;
import edu.gt.miumg.fabrica.servicio.ProcesoManager;

@RestController
@RequestMapping("/api/procesos")
public class ProcesoController {

    private final ProcesoManager manager;

    public ProcesoController(ProcesoManager manager) {
        this.manager = manager;
    }

    @PostMapping
    public String crear() {
        return manager.crearProceso();
    }

    @GetMapping
    public List<String> listar() {
        return manager.listarProcesos();
    }

    @GetMapping("/{id}/estado")
    public EstadoRes estado(@PathVariable String id) {
        ProcesoProduccion p = manager.getProceso(id);
        return new EstadoRes(p.estadoActual(), p.getMateriaPrima() == null ? null : p.getMateriaPrima().toString(), p.esTerminal());
    }

    @GetMapping("/{id}/historial")
    public List<String> historial(@PathVariable String id) {
        return manager.historial(id);
    }

    @PostMapping("/{id}/mp")
    public ResponseEntity<Void> setMateriaPrima(@PathVariable String id, @RequestBody MateriaPrimaReq body) {
        manager.setMateriaPrima(id,
                new MateriaPrima(body.tipo(), body.calidad(), body.humedadPorc(), body.cantidad()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decision")
    public ResponseEntity<Void> decidir(@PathVariable String id, @RequestBody DecisionReq req) {
        manager.setDecision(id, req.decision());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/avanzar")
    public String avanzar(@PathVariable String id) {
        manager.avanzar(id);
        return manager.getProceso(id).estadoActual();
    }

    @PostMapping("/{id}/reiniciar")
    public ResponseEntity<Void> reiniciar(@PathVariable String id) {
        manager.reiniciar(id);
        return ResponseEntity.ok().build();
    }
}
