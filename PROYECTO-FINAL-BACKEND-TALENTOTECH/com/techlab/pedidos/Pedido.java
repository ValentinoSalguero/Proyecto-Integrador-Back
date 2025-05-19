package com.techlab.pedidos;

import java.util.ArrayList;
import java.util.List;

import com.techlab.productos.Producto;
import com.techlab.excepciones.StockInsuficienteException;

public class Pedido {
    private List<LineaPedido> lineas;
    private double total;
    private static int contadorPedidos = 1;
    private int id;

    public Pedido() {
        this.id = contadorPedidos++;
        this.lineas = new ArrayList<>();
        this.total = 0.0;
    }

    public Pedido(List<LineaPedido> lineas, double total) {
        this.lineas = lineas;
        this.total = total;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto.getStock() >= cantidad) {
            LineaPedido linea = new LineaPedido(producto, cantidad);
            lineas.add(linea);
            total += linea.getSubtotal();
            producto.setStock(producto.getStock() - cantidad);
            System.out.println("Producto agregado: " + producto.getNombre() + " x " + cantidad);
        } else {
            throw new StockInsuficienteException("No hay suficiente stock de " + producto.getNombre());
        }
    }

    public void mostrarPedido() {
        if (lineas.isEmpty()) {
            System.out.println("El pedido está vacío.");
        } else {
            System.out.println("--- Detalles del Pedido ---");
            System.out.printf("%-20s %-10s %-10s\n", "Producto", "Cantidad", "Subtotal");
            for (LineaPedido linea : lineas) {
                System.out.printf("%-20s %-10d %-10.2f\n", linea.producto.getNombre(), linea.cantidad, linea.subtotal);
            }
            System.out.println("----------------------------");
            System.out.printf("Total: $%.2f\n", total);
        }
    }

    public double getTotal() {
        return total;
    }

    public int getId() {
        return id;
    }

    private class LineaPedido {
        private Producto producto;
        private int cantidad;
        private double subtotal;
    
        public LineaPedido(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.subtotal = producto.getPrecio()* cantidad;
        }

        public double getSubtotal() {
            return subtotal;
        }

        @Override
        public String toString() {
            return "LineaPedido{" +
                "producto=" + producto.getNombre() + 
                ", cantidad=" + cantidad + 
                ", subtotal=" + subtotal +
                '}';
        }
    }

}

/*
5. Creación de Pedidos 
○ Implementar la clase Pedido (o Orden) que incluya: 
■ Lista de productos (o estructura LineaPedido con producto y 
cantidad). 
○ Solicitar al usuario qué productos desea y en qué cantidad. 
○ Verificar stock; si no alcanza, lanzar una excepción (por ejemplo 
StockInsuficienteException) o mostrar un mensaje de error. 
○ Calcular el costo total (suma de precio * cantidad de cada producto). 
○ Disminuir el stock de cada producto si el pedido se confirma. 
○ (Opcional) Mostrar una lista de pedidos realizados. 
 */