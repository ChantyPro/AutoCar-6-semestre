package model;

/**
 * Vehículo tipo Camión Ligero.
 * Usado para carga o logística urbana eléctrica/híbrida.
 */
public class Camion extends Vehiculo {

    private double capacidadCargaTon; // Capacidad de carga en toneladas

    public Camion(String placa, String marca, String modelo, int anio,
                  double autonomiaKm, String tipoEnergia, double capacidadCargaTon) {
        super(placa, marca, modelo, anio, autonomiaKm, tipoEnergia);
        this.capacidadCargaTon = capacidadCargaTon;
    }

    public double getCapacidadCargaTon() { return capacidadCargaTon; }

    @Override
    public String getTipo() { return "Camion"; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Carga: %.1f ton", capacidadCargaTon);
    }
}
