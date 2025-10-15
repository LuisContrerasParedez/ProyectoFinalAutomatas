package edu.gt.miumg.fabrica.persistencia;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoEventRepo extends JpaRepository<ProcesoEvent, Long> {
  List<ProcesoEvent> findByProcesoIdOrderByIdAsc(UUID procesoId);
}
