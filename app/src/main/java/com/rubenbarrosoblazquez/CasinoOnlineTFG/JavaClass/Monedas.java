package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

public class Monedas {
    private String valor;
    public boolean pulsado;

    public Monedas(String valor, boolean pulsado) {
        this.valor = valor;
        this.pulsado = pulsado;
    }

    public String getValor() {
        return valor;
    }

    public boolean isPulsado() {
        return pulsado;
    }
}
