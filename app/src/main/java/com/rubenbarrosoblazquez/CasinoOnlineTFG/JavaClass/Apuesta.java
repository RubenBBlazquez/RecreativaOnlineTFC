package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

public class Apuesta {
    private String numero;
    private double cantidadApostada;
    private int color;

    public Apuesta(String numero, double cantidadApostada,int color) {
        this.numero = numero;
        this.cantidadApostada = cantidadApostada;
        this.color=color;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
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
}
