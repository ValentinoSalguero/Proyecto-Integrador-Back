package com.techlab.productos;

public class Comida extends Producto {
    private int calorias;

    public Comida(String nombre, double precio, int stock, int calorias) {
        super(nombre, precio, stock);
        this.calorias = calorias;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" %-10d", calorias);
    }
}
