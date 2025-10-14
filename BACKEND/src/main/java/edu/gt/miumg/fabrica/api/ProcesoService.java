package edu.gt.miumg.fabrica.api;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gt.miumg.fabrica.MateriaPrima;
import edu.gt.miumg.fabrica.ProcesoProduccion;
import edu.gt.miumg.fabrica.persistencia.MateriaPrimaEntity;
import edu.gt.miumg.fabrica.persistencia.MateriaPrimaRepo;
import edu.gt.miumg.fabrica.persistencia.ProcesoEvent;
import edu.gt.miumg.fabrica.persistencia.ProcesoEventRepo;

@Service
public class ProcesoService {

  private final ProcesoEventRepo eventRepo;
  private final MateriaPrimaRepo mpRepo;

  private final String procesoId = UUID.randomUUID().toString();
  private final ProcesoProduccion proceso = new ProcesoProduccion();

  public ProcesoService(ProcesoEventRepo eventRepo, MateriaPrimaRepo mpRepo) {
    this.eventRepo = eventRepo;
    this.mpRepo = mpRepo;
  }

  public ProcesoProduccion getProceso() { return proceso; }

  @Transactional
  public void setMateriaPrima(MateriaPrima mp) {
    // Persistimos snapshot
    MateriaPrimaEntity e = new MateriaPrimaEntity();
    e.setTipo(mp.getTipo());
    e.setCalidad(mp.getCalidad());
    e.setHumedad_porc(mp.getHumedadPorc());
    e.setCantidad(mp.getCantidad());
    mpRepo.save(e);

    // Aplica al proceso en memoria
    proceso.setMateriaPrima(mp);
  }

  @Transactional
  public void decidir(ProcesoProduccion.DecisionInspeccion d) {
    proceso.setDecisionInspeccion(d);
  }

  @Transactional
  public void reiniciar() {
    proceso.reiniciar();
  }

  @Transactional
  public void avanzar() {
    String from = proceso.estadoActual();
    MateriaPrima mp = proceso.getMateriaPrima();

    proceso.avanzar();
    String to = proceso.estadoActual();

    ProcesoEvent ev = new ProcesoEvent();
    ev.setProceso_id(procesoId);
    ev.setEstado_origen(from);
    ev.setEstado_destino(to);
    if (mp != null) {
      ev.setMp_tipo(mp.getTipo().name());
      ev.setMp_calidad(mp.getCalidad().name());
      ev.setMp_humedad(mp.getHumedadPorc());
      ev.setMp_cantidad(mp.getCantidad());
    }
    eventRepo.save(ev);
  }
}
