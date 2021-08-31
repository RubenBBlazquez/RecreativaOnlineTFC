package com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash_screen);

        Intent goToLogin = new Intent(this,LoginRegisterActivity.class);
        startActivity(goToLogin);
        finish();
    }
}