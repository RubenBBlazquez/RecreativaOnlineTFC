package com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces;

import com.google.android.gms.ads.AdView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;

public interface OnGetUserInformation {
    public User getUserInformation();
    public void updateBalanceTexts();
    public FirebaseCloudFirestore getFirebaseInstance();
}
