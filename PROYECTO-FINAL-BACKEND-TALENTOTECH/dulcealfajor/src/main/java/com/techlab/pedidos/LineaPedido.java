package com.techlab.pedidos;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techlab.productos.Producto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lineas_pedido")
@Data
@NoArgsConstructor
public class LineaPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    private int cantidad;
    private double subtotal;

    public LineaPedido(Producto producto, int cantidad, Pedido pedido) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecio() * cantidad;
        this.pedido = pedido;
    }
}
