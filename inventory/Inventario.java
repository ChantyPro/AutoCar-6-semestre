package autocar.inventory;

import autocar.model.Vehiculo;

/**
 * Inventario de vehículos de AutoCar.
 *
 * DECISIÓN DE DISEÑO – Escenario 1:
 * Se optó por NO usar Factory Method ni Prototype como único patrón del inventario.
 * El inventario en sí es responsable de almacenar, buscar y ordenar; la creación
 * de vehículos se delega al código cliente (main), que puede instanciar directamente
 * o usar un Factory si el sistema crece. Esto mantiene las responsabilidades separadas
 * (Single Responsibility Principle).
 *
 * Estructura interna: array de objetos de tamaño fijo (MAX_VEHICULOS).
 * Operaciones implementadas:
 *   - agregar(Vehiculo)
 *   - buscarPorPlaca(String)
 *   - ordenarPorAutonomia()   → Selection Sort ascendente
 *   - listar()
 */
public class Inventario {

    private static final int MAX_VEHICULOS = 50;

    private Vehiculo[] vehiculos;
    private int cantidad; // Cuántos vehículos hay actualmente

    public Inventario() {
        vehiculos = new Vehiculo[MAX_VEHICULOS];
        cantidad = 0;
    }

    // ── Agregar ──────────────────────────────────────────────────────────────

    /**
     * Agrega un vehículo al inventario.
     * @throws IllegalStateException si el inventario está lleno.
     * @throws IllegalArgumentException si el vehículo es null o la placa ya existe.
     */
    public void agregar(Vehiculo v) {
        if (v == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null.");
        }
        if (cantidad >= MAX_VEHICULOS) {
            throw new IllegalStateException("El inventario está lleno (máx. " + MAX_VEHICULOS + ").");
        }
        if (buscarPorPlaca(v.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa: " + v.getPlaca());
        }
        vehiculos[cantidad] = v;
        cantidad++;
    }

    // ── Buscar por placa ──────────────────────────────────────────────────────

    /**
     * Búsqueda lineal por placa (case-insensitive).
     * @return el Vehiculo encontrado, o null si no existe.
     */
    public Vehiculo buscarPorPlaca(String placa) {
        if (placa == null) return null;
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].getPlaca().equalsIgnoreCase(placa)) {
                return vehiculos[i];
            }
        }
        return null;
    }

    // ── Ordenar por autonomía (Selection Sort) ────────────────────────────────

    /**
     * Ordena el inventario de menor a mayor autonomía usando Selection Sort.
     * Modifica el array en sitio (in-place).
     */
    public void ordenarPorAutonomia() {
        for (int i = 0; i < cantidad - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < cantidad; j++) {
                if (vehiculos[j].getAutonomiaKm() < vehiculos[minIdx].getAutonomiaKm()) {
                    minIdx = j;
                }
            }
            // Intercambio
            Vehiculo temp = vehiculos[i];
            vehiculos[i] = vehiculos[minIdx];
            vehiculos[minIdx] = temp;
        }
    }

    // ── Listar ────────────────────────────────────────────────────────────────

    /** Imprime todos los vehículos del inventario. */
    public void listar() {
        if (cantidad == 0) {
            System.out.println("  (inventario vacío)");
            return;
        }
        for (int i = 0; i < cantidad; i++) {
            System.out.printf("  %2d. %s%n", i + 1, vehiculos[i]);
        }
    }

    // ── Getters auxiliares ────────────────────────────────────────────────────

    public int getCantidad() { return cantidad; }

    public Vehiculo getVehiculo(int index) {
        if (index < 0 || index >= cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        return vehiculos[index];
    }
}
