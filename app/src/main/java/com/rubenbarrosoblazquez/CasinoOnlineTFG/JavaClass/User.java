package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String apellidos;
    private String direction;
    private String phone;
    private String Verified;
    private String dni;
    private String provider;
    private String pais;
    private float saldo;
    private float saldo_gastado;
    private int tipoUser;
    private boolean dniVerified;
    private boolean telefonoVerified;
    private String token;

    public User(String name, String email, String apellidos, String provider) {
        this.name = name;
        this.email = email;
        this.apellidos = apellidos;
        this.direction="";
        this.phone="";
        this.Verified="false";
        this.dni="";
        this.provider=provider;
        this.pais="";
        this.saldo=0.0f;
        this.tipoUser=0;
        this.dniVerified=false;
        this.telefonoVerified=false;
        this.saldo_gastado=0.0f;
        this.token="";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getSaldo_gastado() {
        return saldo_gastado;
    }

    public void setSaldo_gastado(float saldo_gastado) {
        this.saldo_gastado = saldo_gastado;
    }

    public boolean isDniVerified() {
        return dniVerified;
    }

    public void setDniVerified(boolean dniVerified) {
        this.dniVerified = dniVerified;
    }

    public boolean isTelefonoVerified() {
        return telefonoVerified;
    }

    public void setTelefonoVerified(boolean telefonoVerified) {
        this.telefonoVerified = telefonoVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public int getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(int tipoUser) {
        this.tipoUser = tipoUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direction='" + direction + '\'' +
                ", phone='" + phone + '\'' +
                ", Verified='" + Verified + '\'' +
                ", dni='" + dni + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
