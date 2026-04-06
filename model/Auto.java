package model;

/**
 * Vehículo tipo Auto (sedán, hatchback, etc.).
 * Apto para alquileres urbanos y de corta-media distancia.
 */
public class Auto extends Vehiculo {

    private int numeroPuertas;

    public Auto(String placa, String marca, String modelo, int anio,
                double autonomiaKm, String tipoEnergia, int numeroPuertas) {
        super(placa, marca, modelo, anio, autonomiaKm, tipoEnergia);
        this.numeroPuertas = numeroPuertas;
    }

    public int getNumeroPuertas() { return numeroPuertas; }

    @Override
    public String getTipo() { return "Auto"; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Puertas: %d", numeroPuertas);
    }
}
