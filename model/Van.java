package autocar.model;

/**
 * Vehículo tipo Van (minivan, furgoneta de pasajeros).
 * Pensado para grupos o transporte familiar.
 */
public class Van extends Vehiculo {

    private int capacidadPasajeros;

    public Van(String placa, String marca, String modelo, int anio,
               double autonomiaKm, String tipoEnergia, int capacidadPasajeros) {
        super(placa, marca, modelo, anio, autonomiaKm, tipoEnergia);
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public int getCapacidadPasajeros() { return capacidadPasajeros; }

    @Override
    public String getTipo() { return "Van"; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Pasajeros: %d", capacidadPasajeros);
    }
}
