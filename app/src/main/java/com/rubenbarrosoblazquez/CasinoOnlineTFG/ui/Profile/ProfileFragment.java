package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private OnGetUserInformation mListener;
    private User u;
    private TextView saldo;
    private EditText nombre;
    private EditText apellidos;
    private EditText telefono;
    private EditText direccion;
    private EditText dni;
    private ImageView perfil;
    private TextView email;
    private TextView nombreYApellidos;
    private OnAdsListener mListenerAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.u=mListener.getUserInformation();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        //v.getContext().setTheme(R.style.AppThemeProfile);
        this.saldo=v.findViewById(R.id.textoCasino);
        this.saldo.setText(this.u.getSaldo()+" €");

        this.nombre=v.findViewById(R.id.nombreEditProfile);
        this.nombre.setText(this.u.getName());

        this.apellidos=v.findViewById(R.id.apellidoEditProfile);
        this.apellidos.setText(this.u.getApellidos());

        this.telefono=v.findViewById(R.id.telefonoEditProfile);
        this.telefono.setText(this.u.getPhone());

        this.dni=v.findViewById(R.id.dniEditProfile);
        this.dni.setText(this.u.getDni());

        this.direccion=v.findViewById(R.id.direccionEditProfile);
        this.direccion.setText(this.u.getDirection());


        this.perfil=v.findViewById(R.id.imageProfile);

        this.email=v.findViewById(R.id.emailPerfil);
        this.email.setText(this.u.getEmail());

        this.nombreYApellidos=v.findViewById(R.id.nombreSuperiorPerfil);
        this.nombreYApellidos.setText(this.u.getName()+" "+this.u.getApellidos());

        Button seeAd = v.findViewById(R.id.verAnuncioPerfil);
        seeAd.setOnClickListener(this);

        Button actualizarDatos = v.findViewById(R.id.actualizarDatosPersonales);
        actualizarDatos.setOnClickListener(this);

        this.mListenerAds.rewardedAd();
        this.mListenerAds.loadRewardedVideoAd();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity= (Activity) context;
            this.mListener=(OnGetUserInformation)activity;
            this.mListenerAds=(OnAdsListener)activity;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verAnuncioPerfil:
                Toast.makeText(getContext(), "fsdf", Toast.LENGTH_SHORT).show();
                this.mListenerAds.showAd();
                this.mListener.updateBalanceTexts();
                this.saldo.setText(this.u.getSaldo()+0.5+" €");
                break;
                case R.id.actualizarDatosPersonales:
                    try{
                        User u=mListener.getUserInformation();
                        u.setDni(this.dni.getText().toString());
                        u.setPhone(this.telefono.getText().toString());
                        u.setName(this.nombre.getText().toString());
                        u.setApellidos(this.apellidos.getText().toString());
                        u.setDirection(this.direccion.getText().toString());

                        if(this.mListener.UpdateUserInformation(u)){
                            Navigation.findNavController(v).navigate(R.id.action_nav_profile_self);
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error indeterminado, comprueba los datos que has introducido", Toast.LENGTH_SHORT).show();
                    }

                    break;
        }
    }
}