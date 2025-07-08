package com.techlab.pedidos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.techlab.productos.Producto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import com.techlab.excepciones.StockInsuficienteException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int usuarioId;
    private LocalDateTime fechaPedido;
    
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;
    
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", orphanRemoval = true)
    private List<LineaPedido> lineas;

    public Pedido(int usuarioId) {
        this.usuarioId = usuarioId;
        this.fechaPedido = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.lineas = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay suficiente stock de " + producto.getNombre());
        }

        LineaPedido linea = new LineaPedido(producto, cantidad, this);
        lineas.add(linea);
    }

    public double getCostoTotal() {
        return lineas.stream()
                .mapToDouble(LineaPedido::getSubtotal)
                .sum();
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void mostrarPedido() {
        if (lineas.isEmpty()) {
            System.out.println("El pedido está vacío.");
            return;
        }

        System.out.println("ID Pedido: " + id);
        System.out.println("Usuario ID: " + usuarioId);
        System.out.println("Fecha: " + fechaPedido);
        System.out.println("Estado: " + estado);
        System.out.println("--- Líneas del Pedido ---");
        for (LineaPedido linea : lineas) {
            System.out.printf("  - %s (x%d) - $%.2f%n", linea.getProducto().getNombre(), linea.getCantidad(), linea.getSubtotal());
        }
        System.out.println("------------------------");
        System.out.printf("Total: $%.2f%n", getCostoTotal());
    }
}
