package com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces;

import com.google.android.gms.ads.AdView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseRealTimeModel;

import java.util.ArrayList;

public interface OnGetUserActions {
    public User getUserInformation();
    public void setUserInformation(User u);
    public boolean UpdateUserInformation(User u);
    public void reloadHeaderDraweInfo();
    public void updateBalanceTexts();
    public FirebaseCloudFirestore getFirestoreInstance();
    public FirebaseRealTimeModel getFirestoreRealTimeInstance();
    public CasinoActivity getActivity();
    public void hideActionBar();
    public void showActionBar();
    public double getAdReward();
}
