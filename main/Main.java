package main;

import builder.ContratoAlquiler;
import factory.VehiculoFactory;
import inventory.Inventario;
import model.Vehiculo;
import java.util.Scanner;

/**
 * Punto de entrada de la demostración del sistema AutoCar.
 *
 * Muestra:
 *  1. Gestión del inventario con array de objetos.
 *  2. Búsqueda por placa.
 *  3. Ordenamiento por autonomía.
 *  4. Construcción de contratos con el patrón Builder.
 */
public class Main {

    public static void main(String[] args) {

        // ─────────────────────────────────────────────────────────────────────
        // ESCENARIO 1: Inventario de vehículos
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("=== INVENTARIO DE VEHÍCULOS ===\n");

        Inventario inventario = new Inventario();

        inventario.agregar(VehiculoFactory.crearVehiculo("auto",   "ABC-123", "Toyota",  "Corolla EV",   2023, 430, "electrico",  4));
        inventario.agregar(VehiculoFactory.crearVehiculo("auto",   "DEF-456", "Nissan",  "Leaf",         2022, 385, "electrico",  4));
        inventario.agregar(VehiculoFactory.crearVehiculo("van",    "GHI-789", "Honda",   "Odyssey H",    2023, 500, "hibrido",    8));
        inventario.agregar(VehiculoFactory.crearVehiculo("van",    "JKL-012", "Hyundai", "Staria EV",    2024, 450, "electrico",  7));
        inventario.agregar(VehiculoFactory.crearVehiculo("camion", "MNO-345", "Ford",    "E-Transit",    2023, 317, "electrico",  1.5));
        inventario.agregar(VehiculoFactory.crearVehiculo("camion", "PQR-678", "Renault", "Master E-Tech",2022, 200, "electrico",  1.2));

        System.out.println("Inventario inicial:");
        inventario.listar();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                System.out.println("\n=== MENÚ PRINCIPAL ===");
                System.out.println("1. Agregar vehículo al inventario");
                System.out.println("2. Listar inventario");
                System.out.println("3. Buscar vehículo por placa");
                System.out.println("4. Ordenar inventario por autonomía");
                System.out.println("5. Crear contrato de alquiler");
                System.out.println("6. Salir");

                int opcion = leerEnteroEnRango(scanner, "Seleccione una opción (1-6): ", 1, 6);
                switch (opcion) {
                    case 1:
                        System.out.println("\n=== AÑADIR VEHÍCULO AL INVENTARIO ===");
                        try {
                            agregarVehiculoInteractivo(scanner, inventario);
                            System.out.println("  Vehículo agregado con éxito.");
                        } catch (IllegalStateException | IllegalArgumentException e) {
                            System.out.println("  No se pudo agregar el vehículo: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("\n=== LISTAR INVENTARIO ===");
                        inventario.listar();
                        break;
                    case 3:
                        System.out.println("\n=== BUSCAR VEHÍCULO POR PLACA ===");
                        String placa = leerCadenaNoVacia(scanner, "Placa a buscar: ");
                        Vehiculo encontrado = inventario.buscarPorPlaca(placa);
                        if (encontrado != null) {
                            System.out.println("  Encontrado → " + encontrado);
                        } else {
                            System.out.println("  No se encontró ningún vehículo con esa placa.");
                        }
                        break;
                    case 4:
                        System.out.println("\n=== ORDENAR POR AUTONOMÍA ===");
                        inventario.ordenarPorAutonomia();
                        inventario.listar();
                        break;
                    case 5:
                        System.out.println("\n=== CREAR CONTRATO INTERACTIVO ===");
                        ContratoAlquiler contratoUsuario = crearContratoInteractivo(scanner, inventario);
                        System.out.println(contratoUsuario);
                        break;
                    case 6:
                        salir = true;
                        System.out.println("\nSaliendo del sistema AutoCar. ¡Hasta pronto!");
                        break;
                }
            }
        }

        System.out.println("\n=== DEMOSTRACIÓN DE CONTRATOS Y VALIDACIONES ===\n");
        Vehiculo auto  = inventario.buscarPorPlaca("ABC-123");
        Vehiculo van   = inventario.buscarPorPlaca("GHI-789");
        Vehiculo camion = inventario.buscarPorPlaca("MNO-345");

        if (auto != null && van != null && camion != null) {
            ContratoAlquiler contrato1 = new ContratoAlquiler.ContratoBuilder(
                    "Carlos Rodríguez", auto, "diario", 7)
                    .build();
            System.out.println(contrato1);

            System.out.println();
            ContratoAlquiler contrato2 = new ContratoAlquiler.ContratoBuilder(
                    "Empresa Logística S.A.", van, "mensual", 15)
                    .conGPS()
                    .conSeguro()
                    .build();
            System.out.println(contrato2);

            System.out.println();
            ContratoAlquiler contrato3 = new ContratoAlquiler.ContratoBuilder(
                    "María Gómez", camion, "mensual", 45)
                    .conGPS()
                    .conSeguro()
                    .conCargador()
                    .build();
            System.out.println(contrato3);
        }

        System.out.println("\n=== VALIDACIÓN DEL BUILDER ===\n");
        try {
            new ContratoAlquiler.ContratoBuilder("", auto, "diario", 5).build();
        } catch (IllegalArgumentException e) {
            System.out.println("  Error esperado → " + e.getMessage());
        }
        try {
            new ContratoAlquiler.ContratoBuilder("Juan", auto, "diario", 0).build();
        } catch (IllegalArgumentException e) {
            System.out.println("  Error esperado → " + e.getMessage());
        }
    }

    private static ContratoAlquiler crearContratoInteractivo(Scanner scanner, Inventario inventario) {
        String cliente = leerCadenaNoVacia(scanner, "Nombre del cliente: ");
        Vehiculo vehiculo = null;
        while (vehiculo == null) {
            String placa = leerCadenaNoVacia(scanner, "Placa del vehículo disponible: ");
            vehiculo = inventario.buscarPorPlaca(placa);
            if (vehiculo == null) {
                System.out.println("  Placa no encontrada. Intente de nuevo.");
            }
        }
        String plan = leerPlan(scanner);
        int duracion = leerEnteroMinimo(scanner, "Duración en días (mínimo 1): ", 1);

        ContratoAlquiler.ContratoBuilder builder = new ContratoAlquiler.ContratoBuilder(
                cliente, vehiculo, plan, duracion);
        if (leerSiNo(scanner, "¿Incluir GPS? (s/n): ")) {
            builder.conGPS();
        }
        if (leerSiNo(scanner, "¿Incluir seguro? (s/n): ")) {
            builder.conSeguro();
        }
        if (leerSiNo(scanner, "¿Incluir cargador? (s/n): ")) {
            builder.conCargador();
        }
        return builder.build();
    }

    private static String leerCadenaNoVacia(Scanner scanner, String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("  El valor no puede estar vacío.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    private static int leerEnteroMinimo(Scanner scanner, String mensaje, int minimo) {
        int valor = -1;
        while (valor < minimo) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                valor = Integer.parseInt(linea);
                if (valor < minimo) {
                    System.out.printf("  Debe ingresar un número mayor o igual a %d.%n", minimo);
                }
            } catch (NumberFormatException e) {
                System.out.println("  Entrada inválida. Ingrese un número entero.");
            }
        }
        return valor;
    }

    private static int leerEnteroEnRango(Scanner scanner, String mensaje, int minimo, int maximo) {
        int valor = minimo - 1;
        while (valor < minimo || valor > maximo) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                valor = Integer.parseInt(linea);
                if (valor < minimo || valor > maximo) {
                    System.out.printf("  Debe ingresar un número entre %d y %d.%n", minimo, maximo);
                }
            } catch (NumberFormatException e) {
                System.out.println("  Entrada inválida. Ingrese un número entero.");
            }
        }
        return valor;
    }

    private static boolean leerSiNo(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim().toLowerCase();
            if (linea.equals("s") || linea.equals("si") || linea.equals("y") || linea.equals("yes")) {
                return true;
            }
            if (linea.equals("n") || linea.equals("no")) {
                return false;
            }
            System.out.println("  Respuesta inválida. Use 's' o 'n'.");
        }
    }

    private static String leerPlan(Scanner scanner) {
        while (true) {
            String plan = leerCadenaNoVacia(scanner, "Plan de alquiler (diario/semanal/mensual): ").toLowerCase();
            switch (plan) {
                case "diario":
                case "semanal":
                case "mensual":
                    return plan;
                default:
                    System.out.println("  Plan inválido. Debe ser 'diario', 'semanal' o 'mensual'.");
            }
        }
    }

    private static void agregarVehiculoInteractivo(Scanner scanner, Inventario inventario) {
        String tipo = leerTipoVehiculo(scanner);
        String placa;
        do {
            placa = leerCadenaNoVacia(scanner, "Placa: ");
            if (inventario.buscarPorPlaca(placa) != null) {
                System.out.println("  Ya existe un vehículo con esa placa. Ingrese otra placa.");
                placa = null;
            }
        } while (placa == null);
        String marca = leerCadenaNoVacia(scanner, "Marca: ");
        String modelo = leerCadenaNoVacia(scanner, "Modelo: ");
        int anio = leerEnteroMinimo(scanner, "Año: ", 1900);
        double autonomia = leerDoubleMinimo(scanner, "Autonomía en km: ", 0.1);
        String tipoEnergia = leerTipoEnergia(scanner);

        Vehiculo vehiculo;
        switch (tipo) {
            case "auto":
                int puertas = leerEnteroMinimo(scanner, "Número de puertas: ", 1);
                vehiculo = VehiculoFactory.crearVehiculo(tipo, placa, marca, modelo, anio, autonomia, tipoEnergia, puertas);
                break;
            case "van":
                int pasajeros = leerEnteroMinimo(scanner, "Capacidad de pasajeros: ", 1);
                vehiculo = VehiculoFactory.crearVehiculo(tipo, placa, marca, modelo, anio, autonomia, tipoEnergia, pasajeros);
                break;
            default:
                double carga = leerDoubleMinimo(scanner, "Capacidad de carga en toneladas: ", 0.1);
                vehiculo = VehiculoFactory.crearVehiculo(tipo, placa, marca, modelo, anio, autonomia, tipoEnergia, carga);
        }

        inventario.agregar(vehiculo);
    }

    private static String leerTipoVehiculo(Scanner scanner) {
        while (true) {
            String tipo = leerCadenaNoVacia(scanner, "Tipo de vehículo (auto/van/camion): ").toLowerCase();
            switch (tipo) {
                case "auto":
                case "van":
                case "camion":
                    return tipo;
                default:
                    System.out.println("  Tipo inválido. Elija 'auto', 'van' o 'camion'.");
            }
        }
    }

    private static String leerTipoEnergia(Scanner scanner) {
        while (true) {
            String tipo = leerCadenaNoVacia(scanner, "Tipo de energía (electrico/hibrido): ").toLowerCase();
            if (tipo.equals("electrico") || tipo.equals("hibrido")) {
                return tipo;
            }
            System.out.println("  Energía inválida. Debe ser 'electrico' o 'hibrido'.");
        }
    }

    private static double leerDoubleMinimo(Scanner scanner, String mensaje, double minimo) {
        double valor = Double.NaN;
        while (Double.isNaN(valor) || valor < minimo) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                valor = Double.parseDouble(linea);
                if (valor < minimo) {
                    System.out.printf("  Debe ingresar un número mayor o igual a %.1f.%n", minimo);
                }
            } catch (NumberFormatException e) {
                System.out.println("  Entrada inválida. Ingrese un número válido.");
            }
        }
        return valor;
    }
}
