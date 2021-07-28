package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

import android.graphics.Bitmap;

public class Games {
    private String gameName;
    private Bitmap gameIcon;
    private Bitmap backgroundImage;

    public Games(String gameName, Bitmap gameIcon, Bitmap backgroundImage) {
        this.gameName = gameName;
        this.gameIcon = gameIcon;
        this.backgroundImage = backgroundImage;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Bitmap getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(Bitmap gameIcon) {
        this.gameIcon = gameIcon;
    }

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
