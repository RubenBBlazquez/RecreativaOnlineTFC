package com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;

public interface OnRegisterLogInUserListener {
    public void changeFragmentToRegistered();
    public void backToLoginFragment();
    public void logInOk(User u);
    public void sendEmailVerification(FirebaseUser user);
    public void saveUserInfoInFirestore(User u, FirebaseAuth mAuth);
    public FirebaseCloudFirestore getFirestoreInstance();
}
