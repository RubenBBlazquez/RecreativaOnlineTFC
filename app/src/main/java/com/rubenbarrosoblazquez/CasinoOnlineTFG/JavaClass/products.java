package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class products implements Parcelable {
    private String Descripcion;
    private Bitmap img;
    private String Nombre;
    private int n_bastidor;
    private double precio;
    private String tipo;
    private int Cantidad;

    public products(){
        this.Descripcion="";
        this.img=null;
        this.n_bastidor=0;
        this.Nombre="";
        this.precio=0.0;
        this.tipo="";
    }

    public products(String descripcion, Bitmap DIR_IMG, String nombre, int n_bastidor, double precio, String tipo, int cantidad) {
        Descripcion = descripcion;
        this.img = DIR_IMG;
        Nombre = nombre;
        this.n_bastidor = n_bastidor;
        this.precio = precio;
        this.tipo = tipo;
        Cantidad = cantidad;
    }

    protected products(Parcel in) {
        Descripcion = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        Nombre = in.readString();
        n_bastidor = in.readInt();
        precio = in.readDouble();
        tipo = in.readString();
        Cantidad = in.readInt();
    }

    public static final Creator<products> CREATOR = new Creator<products>() {
        @Override
        public products createFromParcel(Parcel in) {
            return new products(in);
        }

        @Override
        public products[] newArray(int size) {
            return new products[size];
        }
    };

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap DIR_IMG) {
        this.img = DIR_IMG;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getN_bastidor() {
        return n_bastidor;
    }

    public void setN_bastidor(int n_bastidor) {
        this.n_bastidor = n_bastidor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "products{" +
                "Descripcion='" + Descripcion + '\'' +
                ", DIR_IMG='" + img.toString() + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", n_bastidor=" + n_bastidor +
                ", precio=" + precio +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Descripcion);
        parcel.writeParcelable(img, i);
        parcel.writeString(Nombre);
        parcel.writeInt(n_bastidor);
        parcel.writeDouble(precio);
        parcel.writeString(tipo);
        parcel.writeInt(Cantidad);
    }
}
