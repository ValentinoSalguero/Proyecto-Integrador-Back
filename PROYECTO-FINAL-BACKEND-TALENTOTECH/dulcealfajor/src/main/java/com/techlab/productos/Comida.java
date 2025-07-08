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
@DiscriminatorValue("Comida")
public class Comida extends Producto {
    private int calorias;

    public Comida(String nombre, String descripcion, double precio, Categoria categoria, String imagenUrl, int stock, int calorias) {
        super(nombre, descripcion, precio, categoria, imagenUrl, stock);
        this.calorias = calorias;
    }
}