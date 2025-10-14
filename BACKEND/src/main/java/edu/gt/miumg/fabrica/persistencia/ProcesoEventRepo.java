package edu.gt.miumg.fabrica.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoEventRepo extends JpaRepository<ProcesoEvent, Long> {
  List<ProcesoEvent> findAllByOrderByIdAsc();
}
