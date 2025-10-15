package edu.gt.miumg.fabrica.persistencia;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoRepo extends JpaRepository<Proceso, UUID> {}
