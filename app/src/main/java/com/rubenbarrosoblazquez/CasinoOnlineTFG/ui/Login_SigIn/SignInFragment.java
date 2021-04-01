package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Login_SigIn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnRegisterLogInUserListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.LoginRegisterActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SignInFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnRegisterLogInUserListener mListener;
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private EditText apellidos;
    private TextView error_name;
    private TextView error_apellidos;
    private TextView error_email;
    private TextView error_password;
    private TextView error_repeatPassword;
    private Spinner pais;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button volver = v.findViewById(R.id.volver_register);
        this.email = v.findViewById(R.id.email_login);
        this.password = v.findViewById(R.id.password_login);
        this.repeatPassword = v.findViewById(R.id.repeatPassword_register);
        this.name = v.findViewById(R.id.nombre_register);
        this.apellidos = v.findViewById(R.id.apellidos_register);
        this.error_apellidos = v.findViewById(R.id.error_apellidos_register);
        this.error_email = v.findViewById(R.id.error_email_register);
        this.error_repeatPassword = v.findViewById(R.id.error_repeatPassword_register);
        this.error_name = v.findViewById(R.id.error_name_register);
        this.error_password = v.findViewById(R.id.error_password_register);

        Button register = v.findViewById(R.id.register_register);
        register.setOnClickListener(this);

        volver.setOnClickListener(this);

        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.volver_register:
                mListener.backToLoginFragment();
                break;
            case R.id.register_register:
                if (this.validateFields()) {
                    this.register();
                }
                break;


        }
    }

    private void register() {

        mAuth.createUserWithEmailAndPassword(this.email.getText().toString(), this.password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("registerSuccess", "createUserWithEmail:success");
                            mListener.saveUserInfoInFirestore(getCurrentUser(), mAuth);
                            mListener.sendEmailVerification(mAuth.getCurrentUser());
                            mListener.backToLoginFragment();
                            Toast.makeText(getContext(), "Correo de verificación enviado, Revisa el correo, y verifica la cuenta",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("registerFail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Ya hay un usuario con ese correo",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private User getCurrentUser() {
        return new User(this.name.getText().toString(), this.email.getText().toString(), this.apellidos.getText().toString(), "BASIC");
    }


    private boolean validateFields() {
        boolean correctFields = true;

        if (this.apellidos.getText().toString().equals("")) {
            correctFields = false;
            error_apellidos.setText(R.string.error_apellidos_register);
            error_apellidos.setVisibility(View.VISIBLE);
        } else {
            error_apellidos.setVisibility(View.GONE);
        }

        if (this.name.getText().toString().equals("")) {
            correctFields = false;
            error_name.setText(R.string.error_name_register);
            error_name.setVisibility(View.VISIBLE);
        } else {
            error_name.setVisibility(View.GONE);
        }

        if (this.password.getText().toString().length() < 6) {
            correctFields = false;
            error_password.setText(R.string.error_password_register);
            error_password.setVisibility(View.VISIBLE);
        } else {
            error_password.setVisibility(View.GONE);
        }

        if (!this.repeatPassword.getText().toString().equals(this.password.getText().toString())) {
            correctFields = false;
            error_repeatPassword.setText(R.string.error_repeatPassword_register);
            error_repeatPassword.setVisibility(View.VISIBLE);
        } else {
            error_repeatPassword.setVisibility(View.GONE);
        }

        if (this.email.getText().toString().equals("")) {
            correctFields = false;
            error_email.setText(R.string.error_email_register);
            error_email.setVisibility(View.VISIBLE);
        } else {
            error_email.setVisibility(View.GONE);
        }

        return correctFields;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginRegisterActivity) {
            Activity a = (Activity) context;
            mListener = (OnRegisterLogInUserListener) a;
        }
    }


}