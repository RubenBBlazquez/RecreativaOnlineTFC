package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

public class Apuesta {
    private int numero;
    private double cantidadApostada;
    private int color;

    public Apuesta(int numero, double cantidadApostada,int color) {
        this.numero = numero;
        this.cantidadApostada = cantidadApostada;
        this.color=color;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getCantidadApostada() {
        return cantidadApostada;
    }

    public void setCantidadApostada(double cantidadApostada) {
        this.cantidadApostada = cantidadApostada;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Apuesta{" +
                "numero='" + numero + '\'' +
                ", cantidadApostada=" + cantidadApostada +
                ", color=" + color +
                '}';
    }
}
