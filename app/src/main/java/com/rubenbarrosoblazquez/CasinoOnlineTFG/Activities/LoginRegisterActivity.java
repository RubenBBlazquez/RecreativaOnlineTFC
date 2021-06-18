package com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnRegisterLogInUserListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Login_SigIn.LogInFragment;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Login_SigIn.SignInFragment;

import java.util.HashMap;

public class LoginRegisterActivity extends AppCompatActivity implements OnRegisterLogInUserListener {
    private FrameLayout container;
    private User InfoUserLogged;
    //private motionLetter task;
    private FirebaseCloudFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        this.container = findViewById(R.id.container_session);
        this.db = new FirebaseCloudFirestore(getApplicationContext());
        getSupportActionBar().hide();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_session, new LogInFragment())
                .commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
       // task = new motionLetter();
        //task.execute(" CASINO ONLINE");

    }

    @Override
    protected void onStop() {
        super.onStop();
        //task.salir = true;
        Log.d("asynctask", "async parado");

    }

    @Override
    public void changeFragmentToRegistered() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left)
                .replace(R.id.container_session, new SignInFragment())
                .commit();
    }

    @Override
    public void backToLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                .replace(R.id.container_session, new LogInFragment())
                .commit();
    }


    @Override
    public void logInOk(User u) {
        Intent i = new Intent(this, CasinoActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("user", u);
        i.putExtra("bundle", b);
        startActivity(i);
        finish();

    }

    @Override
    public void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Success", "Yes");
                }

            }
        });
    }

    @Override
    public void saveUserInfoInFirestore(User u, FirebaseAuth mAuth) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, String> usuario = new HashMap<>();

        usuario.put("Email", u.getEmail());
        usuario.put("Name", u.getName());
        usuario.put("Last name's", u.getApellidos());
        usuario.put("Direction", u.getDirection());
        usuario.put("Phone", u.getPhone());
        usuario.put("Dni", u.getDni());
        usuario.put("Verified", String.valueOf(mAuth.getCurrentUser().isEmailVerified()));
        usuario.put("Provider", u.getProvider());
        usuario.put("TipoUser", u.getTipoUser() + "");
        usuario.put("DniVerificado",u.isDniVerified()+"");
        usuario.put("TelefonoVerificado",u.isTelefonoVerified()+"");

        db.collection("users").document(u.getEmail()).set(usuario);
        db.collection("users").document(u.getEmail()).update("Saldo",u.getSaldo());
        db.collection("users").document(u.getEmail()).update("SaldoGastado",u.getSaldo_gastado());
    }

    @Override
    public FirebaseCloudFirestore getFirestoreInstance() {
        return this.db;
    }


    /*public class motionLetter extends AsyncTask<String, String, String> {
        public boolean salir = false;

        @Override
        protected String doInBackground(String... strings) {
            while (!salir) {
                String nombre = "";
                for (int i = 0; i < strings[0].length(); i++) {
                    try {
                        nombre += strings[0].charAt(i);
                        publishProgress(nombre);
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return strings[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            TextView t = findViewById(R.id.textoCasino);
            t.setText(values[0]);
            int colores[] = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
            t.setTextColor(colores[(int) (Math.random() * 4)]);
        }
    }*/


}