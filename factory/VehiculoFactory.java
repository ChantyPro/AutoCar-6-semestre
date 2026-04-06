package factory;

import model.Auto;
import model.Camion;
import model.Van;
import model.Vehiculo;

/**
 * Fábrica para crear vehículos usando el patrón Factory Method.
 * Centraliza la lógica de creación de objetos Vehiculo, permitiendo
 * instanciar subclases (Auto, Van, Camion) sin acoplamiento directo.
 */
public class VehiculoFactory {

    /**
     * Crea un vehículo basado en el tipo especificado.
     * @param tipo El tipo de vehículo ("auto", "van", "camion").
     * @param placa Placa del vehículo.
     * @param marca Marca del vehículo.
     * @param modelo Modelo del vehículo.
     * @param anio Año del vehículo.
     * @param autonomiaKm Autonomía en kilómetros.
     * @param tipoEnergia Tipo de energía ("electrico" o "hibrido").
     * @param extra Parámetro adicional específico del tipo:
     *              - Para "auto": número de puertas (int).
     *              - Para "van": capacidad de pasajeros (int).
     *              - Para "camion": capacidad de carga en toneladas (double).
     * @return Una instancia de Vehiculo.
     * @throws IllegalArgumentException Si el tipo es desconocido o el extra no es válido.
     */
    public static Vehiculo crearVehiculo(String tipo, String placa, String marca, String modelo,
                                         int anio, double autonomiaKm, String tipoEnergia, Object extra) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser null.");
        }

        switch (tipo.toLowerCase()) {
            case "auto":
                if (extra instanceof Integer) {
                    return new Auto(placa, marca, modelo, anio, autonomiaKm, tipoEnergia, (Integer) extra);
                } else {
                    throw new IllegalArgumentException("Para 'auto', el parámetro extra debe ser un Integer (número de puertas).");
                }
            case "van":
                if (extra instanceof Integer) {
                    return new Van(placa, marca, modelo, anio, autonomiaKm, tipoEnergia, (Integer) extra);
                } else {
                    throw new IllegalArgumentException("Para 'van', el parámetro extra debe ser un Integer (capacidad de pasajeros).");
                }
            case "camion":
                if (extra instanceof Double) {
                    return new Camion(placa, marca, modelo, anio, autonomiaKm, tipoEnergia, (Double) extra);
                } else {
                    throw new IllegalArgumentException("Para 'camion', el parámetro extra debe ser un Double (capacidad de carga en toneladas).");
                }
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipo + ". Use 'auto', 'van' o 'camion'.");
        }
    }
}