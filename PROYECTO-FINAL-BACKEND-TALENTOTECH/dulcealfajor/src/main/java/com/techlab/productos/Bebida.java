package com.techlab.productos;

import com.techlab.categorias.Categoria;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@DiscriminatorValue("Bebida")
public class Bebida extends Producto {
    private boolean esAlcoholica;

    public Bebida(String nombre, String descripcion, double precio, Categoria categoria, String imagenUrl, int stock, boolean esAlcoholica) {
        super(nombre, descripcion, precio, categoria, imagenUrl, stock);
        this.esAlcoholica = esAlcoholica;
    }
}