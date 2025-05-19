package com.techlab.main;

import java.util.ArrayList;
import java.util.Scanner;

import com.techlab.pedidos.Pedido;
import com.techlab.productos.Producto;
import com.techlab.productos.Bebida;
import com.techlab.productos.Comida;
import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;

public class Main {
    public static void main(String[] args) {
        ArrayList<Producto> productos = new ArrayList<>();
        ArrayList<Pedido> pedidos = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
    
        do {
            try {
                System.out.println("\n--- Menú Principal ---");
                System.out.println("1. Agregar producto");
                System.out.println("2. Listar productos");
                System.out.println("3. Buscar/Actualizar producto");
                System.out.println("4. Eliminar producto");
                System.out.println("5. Crear un pedido");
                System.out.println("6. Listar pedidos");
                System.out.println("7. Salir");
                System.out.print("Selecciona una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine();
        
                switch (opcion) {
                    case 1:
                        agregarProducto(scanner, productos);
                        break;
                    case 2:
                        listarProductos(productos);
                        break;
                    case 3:
                        buscarYActualizarProducto(scanner, productos);
                        break;
                    case 4:
                        eliminarProducto(scanner, productos);
                        break;
                    case 5:
                        crearPedido(scanner, productos, pedidos);
                        break;
                    case 6:
                        listarPedidos(pedidos);
                        break;
                    case 7:
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida, intenta nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine();
            }
        } while (opcion != 7);
    }

    public static void agregarProducto(Scanner scanner, ArrayList<Producto> productos) {
        try {
            System.out.println("¿Qué tipo de producto deseas agregar?");
            System.out.println("1. Bebida");
            System.out.println("2. Comida");
            int tipo = scanner.nextInt();
            scanner.nextLine();

            if (tipo != 1 && tipo != 2) {
                throw new IllegalArgumentException("Tipo de producto no válido.");
            }

            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio del producto: ");
            double precio = scanner.nextDouble();
            System.out.print("Cantidad en stock: ");
            int stock = scanner.nextInt();
            scanner.nextLine();

            int nuevoId = Producto.getContador();
            for (int i = 1; i < Producto.getContador(); i++) {
                final int id = i;
                if (productos.stream().noneMatch(p -> p.getId() == id)) {
                    nuevoId = id;
                    break;
                }
            }

            Producto nuevoProducto;
            if (tipo == 1) {
                System.out.print("¿Es alcohólica? (s/n): ");
                String entrada = scanner.nextLine();
                boolean esAlcoholica = Producto.esAlcoholica(entrada);
                nuevoProducto = new Bebida(nombre, precio, stock, esAlcoholica);
            } else {
                System.out.print("Calorías: ");
                int calorias = scanner.nextInt();
                scanner.nextLine();
                nuevoProducto = new Comida(nombre, precio, stock, calorias);
            }

            nuevoProducto.setId(nuevoId);
            productos.add(nuevoProducto);

            productos.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

            if (nuevoId == Producto.getContador()) {
                Producto.incrementarContador();
            }

            System.out.println("Producto agregado con éxito.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al agregar el producto. Intenta nuevamente.");
            scanner.nextLine();
        }
    }

    public static void listarProductos(ArrayList<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        System.out.println("\n--- Lista de Productos ---");
        System.out.printf("%-5s %-20s %-10s %-10s %-15s\n", "ID", "Nombre", "Precio", "Stock", "Detalle");
        System.out.println("-------------------------------------------------------------");
        for (Producto producto : productos) {
            String detalle = "";
            if (producto instanceof Bebida) {
                detalle = ((Bebida) producto).isEsAlcoholica() ? "Alcohólica" : "No alcohólica";
            } else if (producto instanceof Comida) {
                detalle = ((Comida) producto).getCalorias() + " Calorías";
            }
            System.out.printf("%-5d %-20s %-10.2f %-10d %-15s\n", 
                producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock(), detalle);
        }
    }

    public static void buscarYActualizarProducto(Scanner scanner, ArrayList<Producto> productos) {
        Producto productoEncontrado = null;

        System.out.println("¿Cómo deseas buscar el producto?");
        System.out.println("1. Por ID");
        System.out.println("2. Por nombre");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        while (productoEncontrado == null) {
            try {
                if (opcion == 1) {
                    System.out.print("Ingrese el ID del producto: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    productoEncontrado = buscarProductoPorId(productos, id);
                } else if (opcion == 2) {
                    System.out.print("Ingrese el nombre del producto: ");
                    String nombre = scanner.nextLine();
                    productoEncontrado = buscarProductoPorNombre(productos, nombre);
                } else {
                    System.out.println("Opción no válida. Intenta nuevamente.");
                    return;
                }

                if (productoEncontrado == null) {
                    throw new ProductoNoEncontradoException("Producto no encontrado.");
                }
            } catch (ProductoNoEncontradoException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Entrada inválida. Intenta nuevamente.");
                scanner.nextLine();
            }
        }

        System.out.println("Producto encontrado: " + productoEncontrado.getNombre());

        try {
            System.out.print("¿Deseas actualizar el precio? (s/n): ");
            String actualizarPrecio;
            do {
                actualizarPrecio = scanner.nextLine().trim().toLowerCase();
                if (!actualizarPrecio.equals("s") && !actualizarPrecio.equals("n")) {
                    System.out.println("Entrada inválida. Por favor, responde con 's' o 'n'.");
                }
            } while (!actualizarPrecio.equals("s") && !actualizarPrecio.equals("n"));

            if (actualizarPrecio.equals("s")) {
                System.out.print("Nuevo precio: ");
                double nuevoPrecio = scanner.nextDouble();
                productoEncontrado.setPrecio(nuevoPrecio);
                scanner.nextLine();
            }

            System.out.print("¿Deseas actualizar el stock? (s/n): ");
            String actualizarStock;
            do {
                actualizarStock = scanner.nextLine().trim().toLowerCase();
                if (!actualizarStock.equals("s") && !actualizarStock.equals("n")) {
                    System.out.println("Entrada inválida. Por favor, responde con 's' o 'n'.");
                }
            } while (!actualizarStock.equals("s") && !actualizarStock.equals("n"));

            if (actualizarStock.equals("s")) {
                System.out.print("Nuevo stock: ");
                int nuevoStock = scanner.nextInt();
                productoEncontrado.setStock(nuevoStock);
                scanner.nextLine();
            }

            System.out.println("Producto actualizado: " + productoEncontrado);
        } catch (Exception e) {
            System.out.println("Error al actualizar el producto. Intenta nuevamente.");
            scanner.nextLine();
        }
    }

    public static void eliminarProducto(Scanner scanner, ArrayList<Producto> productos) {
        System.out.println("¿Cómo deseas eliminar el producto?");
        System.out.println("1. Por ID");
        System.out.println("2. Por posición en la lista");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        if (opcion == 1) {
            System.out.print("Ingrese el ID del producto a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Producto productoAEliminar = buscarProductoPorId(productos, id);

            if (productoAEliminar != null) {
                confirmarYEliminarProducto(scanner, productos, productoAEliminar);
            } else {
                throw new ProductoNoEncontradoException("Producto no encontrado.");
            }
        } else if (opcion == 2) {
            System.out.print("Ingrese la posición del producto a eliminar (1 a " + productos.size() + "): ");
            int posicion = scanner.nextInt();
            scanner.nextLine();

            if (posicion > 0 && posicion <= productos.size()) {
                Producto productoAEliminar = productos.get(posicion - 1);
                confirmarYEliminarProducto(scanner, productos, productoAEliminar);
            } else {
                System.out.println("Posición inválida.");
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void confirmarYEliminarProducto(Scanner scanner, ArrayList<Producto> productos, Producto productoAEliminar) {
        System.out.print("¿Estás seguro de que deseas eliminar este producto? (s/n): ");
        String confirmacion;
        do {
            confirmacion = scanner.nextLine().trim().toLowerCase();
            if (!confirmacion.equals("s") && !confirmacion.equals("n")) {
                System.out.println("Entrada inválida. Por favor, responde con 's' o 'n'.");
            }
        } while (!confirmacion.equals("s") && !confirmacion.equals("n"));

        if (confirmacion.equals("s")) {
            productos.remove(productoAEliminar);
            System.out.println("Producto eliminado con éxito.");
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    public static void crearPedido(Scanner scanner, ArrayList<Producto> productos, ArrayList<Pedido> pedidos) {
        Pedido pedido = new Pedido();
        while (true) {
            try {
                System.out.print("Ingresa el ID del producto a agregar al pedido (o -1 para terminar): ");
                int id = scanner.nextInt();
                scanner.nextLine();

                if (id == -1) {
                    break;
                }

                Producto productoSeleccionado = buscarProductoPorId(productos, id);

                if (productoSeleccionado != null) {
                    System.out.print("Cantidad: ");
                    int cantidad = scanner.nextInt();
                    if (cantidad <= 0) {
                        System.out.println("La cantidad debe ser mayor a 0.");
                        continue;
                    }
                    scanner.nextLine();
                    pedido.agregarProducto(productoSeleccionado, cantidad);
                } else {
                    throw new ProductoNoEncontradoException("Producto no encontrado.");
                }
            } catch (StockInsuficienteException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Entrada inválida. Intenta nuevamente.");
                scanner.nextLine();
            }
        }
        pedidos.add(pedido);
        System.out.println("Pedido creado con éxito.");
    }

    public static void listarPedidos(ArrayList<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos realizados.");
            return;
        }

        System.out.println("\n--- Lista de Pedidos ---");
        for (int i = 0; i < pedidos.size(); i++) {
            System.out.println("Pedido #" + (i + 1));
            pedidos.get(i).mostrarPedido();
            System.out.println();
        }
    }

    private static Producto buscarProductoPorId(ArrayList<Producto> productos, int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    private static Producto buscarProductoPorNombre(ArrayList<Producto> productos, String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío. Intenta nuevamente.");
            return null;
        }

        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }
}