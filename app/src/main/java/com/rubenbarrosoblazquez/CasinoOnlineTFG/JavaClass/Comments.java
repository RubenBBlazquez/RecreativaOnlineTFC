package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;

public class Comments {
    private String comment;
    private Bitmap img;
    private String email;

    public Comments(){
        this.comment="";
        this.img=null;
        this.email="";
    }

    public Comments(String comment, Bitmap dirImg, String email) {
        this.comment = comment;
        this.img = dirImg;
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getDirImg() {
        return img;
    }

    public void setDirImg(Bitmap dirImg) {
        img = dirImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
