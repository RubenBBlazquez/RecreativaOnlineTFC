package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class products implements Parcelable {
    private String Descripcion;
    private String Nombre;
    private double precio;
    private String tipo;
    private int Cantidad;
    private String imgName;

    protected products(Parcel in) {
        Descripcion = in.readString();
        Nombre = in.readString();
        imgName = in.readString();
        precio = in.readDouble();
        tipo = in.readString();
        Cantidad = in.readInt();
        img = in.readParcelable(Bitmap.class.getClassLoader());
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

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    private Bitmap img;

    public products(){
        this.Descripcion="";
        this.Nombre="";
        this.precio=0.0;
        this.tipo="";
        this.img=null;
    }

    public products(String imgName,String descripcion, String nombre, double precio, String tipo, int cantidad) {
        Descripcion = descripcion;
        Nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        Cantidad = cantidad;
        this.img=null;
        this.imgName=imgName;
    }


    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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
                ", Nombre='" + Nombre + '\'' +
                ", precio=" + precio +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Descripcion);
        dest.writeString(Nombre);
        dest.writeString(imgName);
        dest.writeDouble(precio);
        dest.writeString(tipo);
        dest.writeInt(Cantidad);
        dest.writeParcelable(img, flags);
    }
}
