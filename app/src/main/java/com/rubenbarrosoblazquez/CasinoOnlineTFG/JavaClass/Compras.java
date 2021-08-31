package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Compras implements Serializable {

    private String date;
    private String name;
    private String price;
    private String unidades;
    private String type;
    private Bitmap imagen;
    private String id;
    private boolean isUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Compras(String date, String name, String price, String unidades, String type) {
        this.date = date;
        this.name = name;
        this.price = price;
        this.unidades = unidades;
        this.type = type;
        this.imagen=null;
        this.isUsed = false;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

