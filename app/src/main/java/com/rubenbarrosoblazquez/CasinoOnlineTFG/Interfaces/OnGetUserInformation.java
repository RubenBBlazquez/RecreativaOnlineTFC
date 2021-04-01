package com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces;

import com.google.android.gms.ads.AdView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;

import java.util.ArrayList;

public interface OnGetUserInformation {
    public User getUserInformation();
    public boolean UpdateUserInformation(User u);
    public void updateBalanceTexts();
    public FirebaseCloudFirestore getFirestoreInstance();
}
