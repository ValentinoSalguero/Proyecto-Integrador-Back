package com.techlab.productos;

import com.techlab.excepciones.NombreNuloOVacioException;
import com.techlab.excepciones.PrecioInvalidoException;
import com.techlab.excepciones.StockNegativoException;

public class Producto {
    private static int contador = 1;

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        this.id = contador++;
        this.nombre = nombre;
        this.precio = precio;
        this.setStock(stock);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new NombreNuloOVacioException("El nombre no puede ser nulo o vacío.");
            }
            this.nombre = nombre;
        } catch (NombreNuloOVacioException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void setPrecio(double precio) {
        try {
            if (precio < 0) {
                throw new PrecioInvalidoException("El precio no puede ser negativo.");
            }
            this.precio = precio;
        } catch (PrecioInvalidoException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void setStock(int stock) {
        try {
            if (stock < 0) {
                throw new StockNegativoException("El stock no puede ser negativo.");
            }
            this.stock = stock;
        } catch (StockNegativoException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public static int getContador() {
        return contador;
    }

    public static void incrementarContador() {
        contador++;
    }

    public static boolean esAlcoholica(String entrada) {
        if (entrada.equalsIgnoreCase("s")) {
            return true;
        } else if (entrada.equalsIgnoreCase("n")) {
            return false;
        } else {
            throw new IllegalArgumentException("Entrada inválida. Debe ser 's' o 'n'.");
        }
    }

    @Override
    public String toString() {
        return String.format("%-5d %-20s %-10.2f %-10d", id, nombre, precio, stock);
    }

}


/*
1. Ingreso de Productos 
○ Implementar una funcionalidad para agregar productos con: 
■ Nombre (String). 
■ Precio (double). 
■ Cantidad en Stock (int). 
○ Almacenarlos en una colección dinámica (ej. ArrayList<Producto>). 
2. Visualización de Productos 
○ Listar todos los productos, mostrando: 
■ ID (autogenerado o posición en la lista). 
■ Nombre, Precio, Stock. 
○ El sistema debe mostrar un menú con la opción de “Listar productos”. 
3. Búsqueda y Actualización 
○ Permitir buscar un producto por nombre o ID. 
○ Si se encuentra, mostrar su información (mínimo: nombre, precio, stock). 
○ (Opcional) Poder actualizar algunos datos (precio o stock), validando que los 
valores sean coherentes (ej., stock no negativo). 
4. Eliminación de Productos 
○ Habilitar la eliminación de un producto por ID o por posición. 
○ (Opcional) Pedir confirmación antes de borrarlo. 
 */