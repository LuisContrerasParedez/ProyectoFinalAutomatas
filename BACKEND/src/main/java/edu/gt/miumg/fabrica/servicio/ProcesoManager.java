package edu.gt.miumg.fabrica.servicio;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.MateriaPrima;
import edu.gt.miumg.fabrica.ProcesoProduccion;
import edu.gt.miumg.fabrica.estados.EstadoFactory;
import edu.gt.miumg.fabrica.persistencia.Proceso;
import edu.gt.miumg.fabrica.persistencia.ProcesoEvent;
import edu.gt.miumg.fabrica.persistencia.ProcesoEventRepo;
import edu.gt.miumg.fabrica.persistencia.ProcesoRepo;

@Service
public class ProcesoManager {

    private final ProcesoRepo repo;
    private final ProcesoEventRepo eventRepo;

    public ProcesoManager(ProcesoRepo repo, ProcesoEventRepo eventRepo) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        if (value == null) return null;
        try { return Enum.valueOf(type, value); }
        catch (IllegalArgumentException ex) { return null; }
    }

    // ----------------- CRUD / Lecturas -----------------

    @Transactional
    public String crearProceso() {
        Proceso p = new Proceso();
        p.setEstadoActual(Estado.RECEPCION_MP.name());
        p.setTerminal(false);
        p = repo.save(p);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(p.getId());
        ev.setEstadoOrigen("-"); // primer evento sin origen
        ev.setEstadoDestino(Estado.RECEPCION_MP.name());
        eventRepo.save(ev);

        return p.getId().toString();
    }

    @Transactional(readOnly = true)
    public List<String> listarProcesos() {
        return repo.findAll().stream().map(e -> e.getId().toString()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Proceso getProcesoEntity(String id) {
        return repo.findById(UUID.fromString(id))
            .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public ProcesoProduccion getProceso(String id) {
        Proceso e = getProcesoEntity(id);
        var p = new ProcesoProduccion();

        p.setEstadoActual(parseEnum(Estado.class, e.getEstadoActual()));
        p.setTerminal(e.isTerminal());

        if (e.getMpTipo() != null) {
            var tipo = parseEnum(MateriaPrima.Tipo.class, e.getMpTipo());
            var calidad = parseEnum(MateriaPrima.Calidad.class, e.getMpCalidad());
            int humedad = e.getMpHumedad() == null ? 0 : e.getMpHumedad().intValue();
            int cantidad = e.getMpCantidad() == null ? 0 : e.getMpCantidad();
            p.setMateriaPrima(new MateriaPrima(tipo, calidad, humedad, cantidad));
        }
        return p;
    }

    // ----------------- Comandos de dominio -----------------

    @Transactional
    public void setMateriaPrima(String id, MateriaPrima mp) {
        Proceso e = getProcesoEntity(id);
        e.setMpTipo(mp.getTipo() != null ? mp.getTipo().name() : null);
        e.setMpCalidad(mp.getCalidad() != null ? mp.getCalidad().name() : null);
        e.setMpHumedad((double) mp.getHumedadPorc());
        e.setMpCantidad(mp.getCantidad());
        repo.save(e);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(UUID.fromString(id));
        ev.setMpTipo(e.getMpTipo());
        ev.setMpCalidad(e.getMpCalidad());
        ev.setMpHumedad(e.getMpHumedad());
        ev.setMpCantidad(e.getMpCantidad());
        eventRepo.save(ev);
    }

    @Transactional
    public void decidirCalidad(String id, boolean apta) {
        Proceso e = getProcesoEntity(id);

        // Construir el agregado de dominio
        var p = getProceso(id);
        Estado origen = p.getEstadoActual();
        if (origen != Estado.INSPECCION_CALIDAD) {
            throw new IllegalStateException("Solo se puede decidir en INSPECCION_CALIDAD (actual: " + origen + ")");
        }

        // Delegar al estado
        p.decidir(apta);
        Estado destino = p.getEstadoActual();

        // Persistir
        e.setEstadoActual(destino.name());
        e.setTerminal(destino == Estado.RECHAZADA || destino == Estado.DISTRIBUIDO_PRODUCCION);
        repo.save(e);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(UUID.fromString(id));
        ev.setEstadoOrigen(origen.name());
        ev.setEstadoDestino(destino.name());
        eventRepo.save(ev);
    }

    @Transactional
    public void avanzar(String id) {
        Proceso e = getProcesoEntity(id);

        var p = getProceso(id);
        Estado origen = p.getEstadoActual();

        // Delegar al estado (el propio estado valida si puede avanzar)
        p.avanzar();
        Estado destino = p.getEstadoActual();

        e.setEstadoActual(destino.name());
        e.setTerminal(destino == Estado.DISTRIBUIDO_PRODUCCION || destino == Estado.RECHAZADA);
        repo.save(e);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(UUID.fromString(id));
        ev.setEstadoOrigen(origen.name());
        ev.setEstadoDestino(destino.name());
        eventRepo.save(ev);
    }

    @Transactional
    public void reiniciar(String id) {
        Proceso e = getProcesoEntity(id);
        e.setEstadoActual(Estado.RECEPCION_MP.name());
        e.setTerminal(false);
        e.setMpTipo(null);
        e.setMpCalidad(null);
        e.setMpHumedad(null);
        e.setMpCantidad(null);
        repo.save(e);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(UUID.fromString(id));
        ev.setEstadoOrigen("-");
        ev.setEstadoDestino(Estado.RECEPCION_MP.name());
        eventRepo.save(ev);
    }

    // Opcional requerido: REPROCESO (desde INSPECCION_CALIDAD -> REPROCESO)
    @Transactional
    public void reprocesar(String id) {
        Proceso e = getProcesoEntity(id);
        var p = getProceso(id);
        Estado origen = p.getEstadoActual();

        if (origen != Estado.INSPECCION_CALIDAD) {
            throw new IllegalStateException("Solo se puede reprocesar desde INSPECCION_CALIDAD (actual: " + origen + ")");
        }

        // Usamos la implementación concreta de estado:
        // EstadoFactory.of(origen) debe ser InspeccionCalidad y exponer reprocesar(...)
        var op = EstadoFactory.of(origen);
        if (op instanceof edu.gt.miumg.fabrica.estados.InspeccionCalidad ic) {
            ic.reprocesar(p); // -> REPROCESO
        } else {
            throw new IllegalStateException("Estado actual no soporta reproceso: " + origen);
        }

        Estado destino = p.getEstadoActual();
        e.setEstadoActual(destino.name());
        e.setTerminal(false);
        repo.save(e);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(UUID.fromString(id));
        ev.setEstadoOrigen(origen.name());
        ev.setEstadoDestino(destino.name());
        eventRepo.save(ev);
    }

    // ----------------- Lecturas auxiliares -----------------

    @Transactional(readOnly = true)
    public List<String> historial(String id) {
        UUID pid = UUID.fromString(id);
        return eventRepo.findByProcesoIdOrderByIdAsc(pid).stream().map(ev -> {
            if (ev.getEstadoOrigen() != null || ev.getEstadoDestino() != null)
                return "ESTADO: %s -> %s".formatted(
                    ev.getEstadoOrigen() == null ? "-" : ev.getEstadoOrigen(),
                    ev.getEstadoDestino() == null ? "-" : ev.getEstadoDestino());
            if (ev.getMpTipo() != null)
                return "MP: %s | %s | %s%% | %s".formatted(
                    ev.getMpTipo(), ev.getMpCalidad(),
                    ev.getMpHumedad() == null ? "?" : ev.getMpHumedad().intValue(),
                    ev.getMpCantidad() == null ? "?" : ev.getMpCantidad());
            return "EVENTO";
        }).toList();
    }
}
