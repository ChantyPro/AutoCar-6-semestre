package autocar.model;

/**
 * Clase base abstracta que representa un vehículo en la flota de AutoCar.
 * Define los atributos comunes a todos los tipos de vehículos eléctricos/híbridos.
 */
public abstract class Vehiculo {

    protected String placa;
    protected String marca;
    protected String modelo;
    protected int anio;
    protected double autonomiaKm;   // Autonomía en kilómetros (clave para ordenar inventario)
    protected String tipoEnergia;   // "electrico" o "hibrido"

    public Vehiculo(String placa, String marca, String modelo, int anio,
                    double autonomiaKm, String tipoEnergia) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.autonomiaKm = autonomiaKm;
        this.tipoEnergia = tipoEnergia;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getPlaca()       { return placa; }
    public String getMarca()       { return marca; }
    public String getModelo()      { return modelo; }
    public int    getAnio()        { return anio; }
    public double getAutonomiaKm() { return autonomiaKm; }
    public String getTipoEnergia() { return tipoEnergia; }

    /** Retorna el tipo concreto del vehículo (Auto, Van, Camion). */
    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("[%s] %s %s %d | %.0f km | %s | Placa: %s",
                getTipo(), marca, modelo, anio, autonomiaKm, tipoEnergia, placa);
    }
}
