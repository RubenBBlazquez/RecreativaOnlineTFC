package com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass;

public class AdReward {
    private String adType;
    private double reward;

    public AdReward(String adType, double reward) {
        this.adType = adType;
        this.reward = reward;
    }

    public AdReward() {
        this.adType = "";
        this.reward = 0.0;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
}
