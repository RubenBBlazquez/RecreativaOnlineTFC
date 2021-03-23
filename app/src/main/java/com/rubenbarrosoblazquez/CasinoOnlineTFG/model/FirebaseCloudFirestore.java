package com.rubenbarrosoblazquez.CasinoOnlineTFG.model;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseCloudFirestore {

    private Context context;
    private FirebaseFirestore mFirebaseFirestore;

    public FirebaseCloudFirestore(Context context) {
        this.context = context;
        this.mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void updateSaldo(String user,Float saldo) {
        this.mFirebaseFirestore.collection("users").document(user).update("Saldo",String.valueOf(saldo));
    }
}
