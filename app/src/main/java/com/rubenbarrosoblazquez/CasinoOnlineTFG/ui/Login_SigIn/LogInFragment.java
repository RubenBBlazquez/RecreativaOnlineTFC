package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Login_SigIn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.LoginRegisterActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnRegisterLogInUserListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.concurrent.Executor;

public class LogInFragment extends Fragment implements View.OnClickListener, OnCompleteListener<AuthResult>, OnSuccessListener<DocumentSnapshot>, Runnable {

    private OnRegisterLogInUserListener mListener;
    private EditText email;
    private EditText password;
    private TextView errorEmail;
    private TextView errorPassword;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LinearLayout container;
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    public static boolean registrado;
    private static final int GOOGLE_SIGN_IN = 100;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        activeSession();

    }

    public void activeSession() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.prefs_login), Context.MODE_PRIVATE);
        preferences.edit().clear();
        String email = preferences.getString("email", null);
        String provider = preferences.getString("provider", null);
        Log.d("preferences", email + " " + provider);

        if (email != null && provider != null) {
            loginWays(email);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //FacebookSdk.sdkInitialize(getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        Button registrar = v.findViewById(R.id.register_register);
        Button login = v.findViewById(R.id.login);
        ImageButton loginGoogle = v.findViewById(R.id.logingoogle);
        ImageButton loginFacebook = v.findViewById(R.id.loginfacebook);
        ImageButton loginTwitter = v.findViewById(R.id.loginTwitter);
        this.email = (EditText) v.findViewById(R.id.email_login);
        this.password = (EditText) v.findViewById(R.id.password_login);
        this.errorEmail = v.findViewById(R.id.error_email_login);
        this.errorPassword = v.findViewById(R.id.error_password_login);
        this.container = v.findViewById(R.id.container_login_fragment);
        //this.loginButton=(LoginButton)v.findViewById(R.id.loginfacebookLoginButtonGone);

        registrar.setOnClickListener(this);
        login.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
        loginTwitter.setOnClickListener(this);
        loginFacebook.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_register:
                mListener.changeFragmentToRegistered();
                break;
            case R.id.login:

                if (this.ValidateLoginAndPassword()) {
                    this.logInBasic();
                }
                break;

            case R.id.logingoogle:

                loginWithGoogle();
                break;

            case R.id.loginfacebook:
            case R.id.loginTwitter:

                Toast.makeText(getContext(),getString(R.string.funcionalidadNoImplementada), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private boolean ValidateLoginAndPassword() {
        boolean validate = true;

        if (this.email.getText().toString().equals("")) {
            validate = false;
            this.errorEmail.setText(R.string.error_email_register);
            this.errorEmail.setVisibility(View.VISIBLE);
        } else {
            this.errorEmail.setVisibility(View.GONE);
        }

        if (this.password.getText().toString().equals("")) {
            validate = false;
            this.errorPassword.setText(R.string.error_password_login);
            this.errorPassword.setVisibility(View.VISIBLE);
        } else {
            this.errorPassword.setVisibility(View.GONE);
        }

        return validate;
    }

    private void logInBasic() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d("TAG", "signInWithEmail:success");

            this.loginWays(this.email.getText().toString());

        } else {
            // If sign in fails, display a message to the user.
            Log.w("TAG", "signInWithEmail:failure", task.getException());
            Toast.makeText(getContext(), "Usuario o Contraseña Incorrectos",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void loginWays(String email) {

        db.collection("users").document(email).get().addOnSuccessListener(this);

    }


    @Override
    public void onSuccess(DocumentSnapshot d) {
        try {
            Log.d("user", String.valueOf(this.mAuth.getCurrentUser().isEmailVerified()));
            if (this.mAuth.getCurrentUser().isEmailVerified()) {
                User u;

                u = new User(String.valueOf(d.get("Name")), String.valueOf(d.get("Email")), String.valueOf(d.get("Last name's")), String.valueOf(d.get("Provider")));
                u.setVerified(String.valueOf(this.mAuth.getCurrentUser().isEmailVerified()));
                u.setDirection((String) d.get("Direction"));
                u.setPhone((String) d.get("Phone"));
                u.setDni((String) d.get("Dni"));
                u.setSaldo(Float.valueOf(String.valueOf(d.get("Saldo"))));
                u.setSaldo_gastado(Float.valueOf(String.valueOf(d.get("SaldoGastado"))));
                u.setTipoUser(Integer.parseInt(d.getString("TipoUser")));
                u.setTelefonoVerified(Boolean.valueOf(d.getString("TelefonoVerificado")));
                u.setDniVerified(Boolean.valueOf(d.getString("DniVerificado")));
                u.setSkip(d.getBoolean("isSkip"));

                Toast.makeText(this.getContext(), ""+u.isSkip(), Toast.LENGTH_SHORT).show();

                addDataToSharedPreferences(u);
                db.collection("users").document(u.getEmail()).update("Verified", "true");
                mListener.logInOk(u);

            } else {
                Toast.makeText(getContext(), getString(R.string.email_send), Toast.LENGTH_SHORT).show();
                Thread checkVerification = new Thread(this);
                checkVerification.start();
                this.mListener.sendEmailVerification(this.mAuth.getCurrentUser());

            }
        }catch(Exception exp){
            Log.d("error",exp.getMessage());
            SharedPreferences.Editor preferences = this.getActivity().getSharedPreferences(getString(R.string.prefs_login), Context.MODE_PRIVATE).edit();
            preferences.clear();
            preferences.apply();
        }
    }

    private void loginWithGoogle() {
        // GoogleSignInOptions googleconf= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken();
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void googleInfoAccount(Intent data) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d("Hola", "e");

            final GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null) {

                Log.d("google", "firebaseAuthWithGoogle:" + account.getId());
                AuthCredential credet = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                mAuth.signInWithCredential(credet).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        UserExists(account);
                    }
                });
            }

        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w("google", "Google sign in failed");
            // ...
        }
    }

    private void UserExists(final GoogleSignInAccount account) {
        try {
            this.db.collection("users").document(account.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()) {
                        User u = new User(account.getDisplayName(), account.getEmail(), account.getFamilyName(), "GOOGLE");
                        mListener.saveUserInfoInFirestore(u, mAuth);
                        loginWays(account.getEmail());
                    } else {
                        loginWays(account.getEmail());
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "error indeterminado", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginWithFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.performClick();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "error check your internet connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "error check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("facebook", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String nombre = user.getDisplayName();
                            User u = new User(nombre, email, nombre, "FACEBOOK");
                            mListener.saveUserInfoInFirestore(u, mAuth);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed. check your internet connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void addDataToSharedPreferences(User u) {
        Log.d("preferences", u.toString());
        SharedPreferences.Editor preferences = getActivity().getSharedPreferences(getString(R.string.prefs_login), Context.MODE_PRIVATE).edit();
        preferences.putString("email", u.getEmail());
        preferences.putString("provider", u.getProvider());
        preferences.apply();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Hola", "u");

        if (requestCode == GOOGLE_SIGN_IN) {
            this.googleInfoAccount(data);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginRegisterActivity) {
            Activity a = (Activity) context;
            this.mListener = (OnRegisterLogInUserListener) a;
        }
    }

    @Override
    public void run() {
        boolean salir = false;
        while (!salir) {
            this.mAuth.getCurrentUser().reload();
            try {
                Thread.sleep(2000);
                Log.d("hiloVerificacion","g");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.mAuth.getCurrentUser().isEmailVerified()) {
                salir = true;
            }
        }
        //Toast.makeText(getContext(), getString(R.string.email_Verified), Toast.LENGTH_SHORT).show();
        loginWays(this.mAuth.getCurrentUser().getEmail());
    }
}