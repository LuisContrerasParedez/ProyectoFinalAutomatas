package edu.gt.miumg.fabrica;

public interface Estado {
    String obtenerNombre();
    void avanzar(ProcesoProduccion proceso);
}
