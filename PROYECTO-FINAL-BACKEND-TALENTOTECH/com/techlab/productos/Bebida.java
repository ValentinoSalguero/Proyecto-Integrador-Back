package com.techlab.productos;

public class Bebida extends Producto {
    private boolean esAlcoholica;

    public Bebida(String nombre, double precio, int stock, boolean esAlcoholica) {
        super(nombre, precio, stock);
        this.esAlcoholica = esAlcoholica;
    }

    public boolean isEsAlcoholica() {
        return esAlcoholica;
    }

    public void setEsAlcoholica(boolean esAlcoholica) {
        this.esAlcoholica = esAlcoholica;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" %-10s", esAlcoholica ? "Sí" : "No");
    }
}
