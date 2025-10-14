package edu.gt.miumg.fabrica;

public class MateriaPrima {

    public enum Tipo { PINO, CAOBA, ROBLE, MDF, OTRO }
    public enum Calidad { ALTA, MEDIA, BAJA }

    private Tipo tipo;
    private Calidad calidad;
    private double humedadPorc; 
    private int cantidad;        

    public MateriaPrima(Tipo tipo, Calidad calidad, double humedadPorc, int cantidad) {
        this.tipo = tipo;
        this.calidad = calidad;
        this.humedadPorc = humedadPorc;
        this.cantidad = cantidad;
    }

    public Tipo getTipo() { return tipo; }
    public Calidad getCalidad() { return calidad; }
    public double getHumedadPorc() { return humedadPorc; }
    public int getCantidad() { return cantidad; }

    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public void setCalidad(Calidad calidad) { this.calidad = calidad; }
    public void setHumedadPorc(double humedadPorc) { this.humedadPorc = humedadPorc; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return "MateriaPrima{" +
                "tipo=" + tipo +
                ", calidad=" + calidad +
                ", humedad=" + humedadPorc + "%" +
                ", cantidad=" + cantidad +
                '}';
    }
}
