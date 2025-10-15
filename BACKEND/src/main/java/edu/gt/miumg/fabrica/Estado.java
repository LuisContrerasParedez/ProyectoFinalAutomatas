package edu.gt.miumg.fabrica;

/** Estados del ciclo de producción */
public enum Estado {
    RECEPCION_MP,
    INSPECCION_CALIDAD,
    CORTE_DIMENSIONADO,
    SECADO_TRATAMIENTO,
    ALMACENADO,
    DISTRIBUIDO_PRODUCCION,
    RECHAZADA
}
