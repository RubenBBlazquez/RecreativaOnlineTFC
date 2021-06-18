package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

public class Comments {
    private String comment;
    private String DirImg;
    private String email;

    public Comments(){
        this.comment="";
        this.DirImg="";
        this.email="";
    }

    public Comments(String comment, String dirImg, String email) {
        this.comment = comment;
        DirImg = dirImg;
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDirImg() {
        return DirImg;
    }

    public void setDirImg(String dirImg) {
        DirImg = dirImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
