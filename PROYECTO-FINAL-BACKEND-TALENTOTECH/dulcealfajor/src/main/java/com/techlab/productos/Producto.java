package com.techlab.productos;

import com.techlab.categorias.Categoria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_producto", discriminatorType = DiscriminatorType.STRING)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String nombre;
    private String descripcion;
    private double precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    private String imagenUrl;
    private int stock;

    public Producto(String nombre, String descripcion, double precio, Categoria categoria, String imagenUrl, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.stock = stock;
    }
}
