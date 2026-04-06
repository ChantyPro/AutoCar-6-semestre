package builder;

import model.Vehiculo;
import java.util.ArrayList;
import java.util.List;

/**
 * Producto final del patrón Builder.
 *
 * Representa un contrato de alquiler con:
 *   - Datos obligatorios: cliente, vehículo, plan, duración.
 *   - Datos opcionales: GPS, seguro, cargador portátil.
 *   - Regla de negocio: descuento del 10 % si duración > 30 días.
 *
 * El constructor es privado; solo ContratoBuilder puede instanciar esta clase.
 */
public class ContratoAlquiler {

    // ── Campos obligatorios ───────────────────────────────────────────────────
    private final String cliente;
    private final Vehiculo vehiculo;
    private final String planAlquiler;   // "diario", "semanal", "mensual"
    private final int duracionDias;

    // ── Campos opcionales ─────────────────────────────────────────────────────
    private final boolean incluyeGPS;
    private final boolean incluyeSeguro;
    private final boolean incluyCargador;

    // ── Calculado ─────────────────────────────────────────────────────────────
    private final double precioFinal;
    private final boolean descuentoAplicado;

    /** Solo el Builder puede construir un ContratoAlquiler. */
    private ContratoAlquiler(ContratoBuilder builder) {
        this.cliente        = builder.cliente;
        this.vehiculo       = builder.vehiculo;
        this.planAlquiler   = builder.planAlquiler;
        this.duracionDias   = builder.duracionDias;
        this.incluyeGPS     = builder.incluyeGPS;
        this.incluyeSeguro  = builder.incluyeSeguro;
        this.incluyCargador = builder.incluyCargador;

        // Regla de negocio: >30 días → 10 % de descuento
        double precio = calcularPrecioBase(builder);
        if (duracionDias > 30) {
            precio *= 0.90;
            this.descuentoAplicado = true;
        } else {
            this.descuentoAplicado = false;
        }
        this.precioFinal = precio;
    }

    /** Calcula el precio base sumando plan + accesorios. */
    private double calcularPrecioBase(ContratoBuilder b) {
        double tarifa;
        switch (b.planAlquiler.toLowerCase()) {
            case "diario":   tarifa = 50.0 * b.duracionDias;  break;
            case "semanal":  tarifa = 45.0 * b.duracionDias;  break;
            case "mensual":  tarifa = 40.0 * b.duracionDias;  break;
            default:         tarifa = 50.0 * b.duracionDias;
        }
        if (b.incluyeGPS)     tarifa += 5.0  * b.duracionDias;
        if (b.incluyeSeguro)  tarifa += 10.0 * b.duracionDias;
        if (b.incluyCargador) tarifa += 3.0  * b.duracionDias;
        return tarifa;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String   getCliente()        { return cliente; }
    public Vehiculo getVehiculo()       { return vehiculo; }
    public String   getPlanAlquiler()   { return planAlquiler; }
    public int      getDuracionDias()   { return duracionDias; }
    public boolean  isIncluyeGPS()      { return incluyeGPS; }
    public boolean  isIncluyeSeguro()   { return incluyeSeguro; }
    public boolean  isIncluyCargador()  { return incluyCargador; }
    public double   getPrecioFinal()    { return precioFinal; }
    public boolean  isDescuentoAplicado() { return descuentoAplicado; }

    @Override
    public String toString() {
        List<String> accesorios = new ArrayList<>();
        if (incluyeGPS)     accesorios.add("GPS");
        if (incluyeSeguro)  accesorios.add("Seguro");
        if (incluyCargador) accesorios.add("Cargador");

        return String.format(
            "╔══════════════════════════════════════════╗%n" +
            "  CONTRATO DE ALQUILER – AutoCar%n" +
            "  Cliente   : %s%n" +
            "  Vehículo  : %s%n" +
            "  Plan      : %s (%d días)%n" +
            "  Accesorios: %s%n" +
            "  Descuento : %s%n" +
            "  TOTAL     : $%.2f COP%n" +
            "╚══════════════════════════════════════════╝",
            cliente,
            vehiculo.getMarca() + " " + vehiculo.getModelo() + " [" + vehiculo.getPlaca() + "]",
            planAlquiler, duracionDias,
            accesorios.isEmpty() ? "Ninguno" : String.join(", ", accesorios),
            descuentoAplicado ? "10 % aplicado (>30 días)" : "No aplica",
            precioFinal
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INNER CLASS: Builder
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Builder de ContratoAlquiler.
     *
     * PATRÓN BUILDER – Escenario 2:
     * Permite construir el contrato paso a paso, estableciendo primero los datos
     * obligatorios y luego añadiendo accesorios de forma fluida (fluent API).
     * Centraliza las validaciones y la regla de descuento, evitando constructores
     * telescópicos y estados inválidos (e.g., contrato sin cliente o sin vehículo).
     *
     * Uso:
     *   ContratoAlquiler c = new ContratoAlquiler.ContratoBuilder("Ana", auto, "diario", 7)
     *       .conGPS()
     *       .conSeguro()
     *       .build();
     */
    public static class ContratoBuilder {

        // Obligatorios
        private final String   cliente;
        private final Vehiculo vehiculo;
        private final String   planAlquiler;
        private final int      duracionDias;

        // Opcionales (false por defecto)
        private boolean incluyeGPS     = false;
        private boolean incluyeSeguro  = false;
        private boolean incluyCargador = false;

        /**
         * Constructor con campos obligatorios.
         * @param cliente      Nombre del cliente.
         * @param vehiculo     Vehículo del inventario.
         * @param planAlquiler "diario", "semanal" o "mensual".
         * @param duracionDias Número de días del contrato (mínimo 1).
         */
        public ContratoBuilder(String cliente, Vehiculo vehiculo,
                               String planAlquiler, int duracionDias) {
            if (cliente == null || cliente.isBlank())
                throw new IllegalArgumentException("El cliente no puede estar vacío.");
            if (vehiculo == null)
                throw new IllegalArgumentException("El vehículo no puede ser null.");
            if (planAlquiler == null || planAlquiler.isBlank())
                throw new IllegalArgumentException("El plan de alquiler es obligatorio.");
            if (duracionDias < 1)
                throw new IllegalArgumentException("La duración debe ser al menos 1 día.");

            this.cliente      = cliente;
            this.vehiculo     = vehiculo;
            this.planAlquiler = planAlquiler;
            this.duracionDias = duracionDias;
        }

        /** Agrega GPS al contrato. */
        public ContratoBuilder conGPS() {
            this.incluyeGPS = true;
            return this;
        }

        /** Agrega seguro al contrato. */
        public ContratoBuilder conSeguro() {
            this.incluyeSeguro = true;
            return this;
        }

        /** Agrega cargador portátil al contrato. */
        public ContratoBuilder conCargador() {
            this.incluyCargador = true;
            return this;
        }

        /**
         * Construye y retorna el ContratoAlquiler validado.
         * Aplica automáticamente el descuento si duracion > 30 días.
         */
        public ContratoAlquiler build() {
            return new ContratoAlquiler(this);
        }
    }
}
