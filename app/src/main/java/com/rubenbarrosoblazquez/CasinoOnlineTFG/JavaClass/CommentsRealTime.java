package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;

public class CommentsRealTime {
    private String comment;
    private String email;
    private String product;

    public CommentsRealTime(){
        this.comment="";
        this.email="";
    }

    public CommentsRealTime(String comment, String email,String producto) {
        this.comment = comment;
        this.email = email;
        this.product=producto;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CommentsRealTime{" +
                "comment='" + comment + '\'' +
                ", email='" + email + '\'' +
                ", product='" + product + '\'' +
                '}';
    }
}
