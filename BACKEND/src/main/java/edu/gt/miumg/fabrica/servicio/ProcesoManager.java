package edu.gt.miumg.fabrica.servicio;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gt.miumg.fabrica.Estado;
import edu.gt.miumg.fabrica.MateriaPrima;
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

    private static final Map<Estado, Set<Estado>> TRANSICIONES = Map.of(
        Estado.RECEPCION_MP,       Set.of(Estado.INSPECCION_CALIDAD),
        Estado.INSPECCION_CALIDAD, Set.of(Estado.CORTE_DIMENSIONADO, Estado.RECHAZADA),
        Estado.CORTE_DIMENSIONADO, Set.of(Estado.SECADO_TRATAMIENTO),
        Estado.SECADO_TRATAMIENTO, Set.of(Estado.ALMACENADO),
        Estado.ALMACENADO,         Set.of(Estado.DISTRIBUIDO_PRODUCCION)
    );

    private static void validarTransicion(Estado origen, Estado destino) {
        var destinos = TRANSICIONES.getOrDefault(origen, Set.of());
        if (!destinos.contains(destino)) {
            throw new IllegalStateException("Transición inválida: " + origen + " -> " + destino);
        }
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        if (value == null) return null;
        try { return Enum.valueOf(type, value); }
        catch (IllegalArgumentException ex) { return null; }
    }

    @Transactional
    public String crearProceso() {
        Proceso p = new Proceso();
        p.setEstadoActual(Estado.RECEPCION_MP.name());
        p.setTerminal(false);
        p = repo.save(p);

        ProcesoEvent ev = new ProcesoEvent();
        ev.setProcesoId(p.getId());
        ev.setEstadoOrigen("-"); // evita NOT NULL
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
    public edu.gt.miumg.fabrica.ProcesoProduccion getProceso(String id) {
        Proceso e = getProcesoEntity(id);
        var p = new edu.gt.miumg.fabrica.ProcesoProduccion();

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
        Estado origen = parseEnum(Estado.class, e.getEstadoActual());
        if (origen != Estado.INSPECCION_CALIDAD) {
            throw new IllegalStateException("Solo se puede decidir en INSPECCION_CALIDAD (actual: " + origen + ")");
        }
        Estado destino = apta ? Estado.CORTE_DIMENSIONADO : Estado.RECHAZADA;
        validarTransicion(origen, destino);

        e.setEstadoActual(destino.name());
        e.setTerminal(destino == Estado.RECHAZADA);
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
        Estado origen = parseEnum(Estado.class, e.getEstadoActual());
        Estado destino;

        switch (origen) {
            case RECEPCION_MP -> destino = Estado.INSPECCION_CALIDAD;
            case CORTE_DIMENSIONADO -> destino = Estado.SECADO_TRATAMIENTO;
            case SECADO_TRATAMIENTO -> destino = Estado.ALMACENADO;
            case ALMACENADO -> destino = Estado.DISTRIBUIDO_PRODUCCION;
            case INSPECCION_CALIDAD ->
                throw new IllegalStateException("Desde INSPECCION_CALIDAD usa decidirCalidad(true|false).");
            default -> throw new IllegalStateException("No se puede avanzar desde estado: " + origen);
        }

        validarTransicion(origen, destino);
        e.setEstadoActual(destino.name());
        e.setTerminal(destino == Estado.DISTRIBUIDO_PRODUCCION);
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
