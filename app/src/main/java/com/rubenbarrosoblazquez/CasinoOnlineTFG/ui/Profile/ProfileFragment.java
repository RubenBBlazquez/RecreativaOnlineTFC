package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageAnalysis;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private OnGetUserInformation mListener;
    private User u;
    public TextView saldo;
    private EditText nombre;
    private EditText apellidos;
    private EditText telefono;
    private EditText direccion;
    private EditText dni;
    private ImageView perfil;
    private TextView email;
    private TextView nombreYApellidos;
    private OnAdsListener mListenerAds;
    private AlertDialog dialog;
    private EditText verifiedSmsCode;
    private String verificationPhoneId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        if (ContextCompat.checkSelfPermission(
                this.getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            this.getActivity().requestPermissions(new String[] { Manifest.permission.CAMERA },
                    3);
        }
        if (ContextCompat.checkSelfPermission(
                this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            this.getActivity().requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    3);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        this.u=mListener.getUserInformation();
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

        if(u.getProfilePic()!=null){
            perfil.setImageBitmap(u.getProfilePic());
        }else{
            this.mListener.getFirestoreInstance().getProfilePicFromStorage(perfil,this.u.getEmail());
        }

        this.email=v.findViewById(R.id.emailPerfil);
        this.email.setText(this.u.getEmail());

        this.nombreYApellidos=v.findViewById(R.id.nombreSuperiorPerfil);
        this.nombreYApellidos.setText(this.u.getName()+" "+this.u.getApellidos());

        Button seeAd = v.findViewById(R.id.verAnuncioPerfil);
        seeAd.setOnClickListener(this);

        Button actualizarDatos = v.findViewById(R.id.actualizarDatosPersonales);
        actualizarDatos.setOnClickListener(this);

        Button validartelefono= v.findViewById(R.id.validarTelefono);
        validartelefono.setOnClickListener(this);

        Button validarDni = v.findViewById(R.id.sacarFotoDni);
        validarDni.setOnClickListener(this);

        Button getProfilePic = v.findViewById(R.id.sacarFotoPerfil);
        getProfilePic.setOnClickListener(this);

        this.mListenerAds.rewardedAd();
        this.mListenerAds.loadRewardedVideoAd();

        mListener.getActivity().setProfileFragment(this);

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

                this.mListenerAds.showAd();
                this.mListener.updateBalanceTexts();
                //this.saldo.setText(this.u.getSaldo()+0.5+" €");
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
                            mListener.setUserInformation(u);
                            mListener.reloadHeaderDraweInfo();
                            Toast.makeText(getContext(), "datos personales actualizados correctamente", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error indeterminado, comprueba los datos que has introducido", Toast.LENGTH_SHORT).show();
                    }

                    break;
                    case R.id.sacarFotoPerfil:


                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(pickPhoto, 1);
                                }


                        break;

            case R.id.validarTelefono:

                verifiedPhone();

                break;
            case R.id.sacarFotoDni:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_take_or_get_photo, null);

                TextView camera1 =(TextView) view.findViewById(R.id.takePhoto);
                TextView gallery1 = (TextView) view.findViewById(R.id.photoGallery);

                camera1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 3);
                        }
                    }
                });

                gallery1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(pickPhoto, 2);
                        }
                    }
                });
                builder1.setView(view);
                builder1.create();
                builder1.show();

                break;
        }
    }

    private void verifiedPhone(){
        if(!u.getPhone().isEmpty() && !u.isTelefonoVerified()){


            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Toast.makeText(getContext(), getString(R.string.codePhoneSentVerified)+" --> "+credential.getSmsCode(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(getContext(), getString(R.string.codePhoneSentError), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    verificationPhoneId=verificationId;
                    Toast.makeText(getContext(), getString(R.string.codePhoneSent), Toast.LENGTH_SHORT).show();
                    dialogVerifyphone();
                }
            };


            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber(u.getPhone())       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }else{
            if(u.isTelefonoVerified()){
                Toast.makeText(getContext(), getString(R.string.phoneAlreadyVerified), Toast.LENGTH_SHORT).show();
            }else if(u.getPhone().isEmpty()){
                Toast.makeText(getContext(), getString(R.string.noPhone), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dialogVerifyphone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.dialog_verified_phone_number, null);

        verifiedSmsCode=v.findViewById(R.id.verifiedSmsCodeEditText);

        Button cerrarDialogo = v.findViewById(R.id.verificarTelefonoDialog);
        cerrarDialogo.setOnClickListener(this);

        builder.setView(v);
        builder.create();
        dialog = builder.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == this.getActivity().RESULT_OK){
            Uri imageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
                this.perfil.setImageBitmap(imageBitmap);
                this.u.setProfilePic(imageBitmap);
                this.mListener.getFirestoreInstance().saveProfilePic(imageBitmap,this.u);
                mListener.setUserInformation(u);
                mListener.reloadHeaderDraweInfo();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(requestCode==2 && resultCode == this.getActivity().RESULT_OK){

            Uri imageUri = data.getData();
            try {
                textRecognizer(imageUri);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(requestCode==3 && resultCode == this.getActivity().RESULT_OK){

            Bundle extras = data.getExtras();
            textRecognizer(getImageUri(getContext(), (Bitmap) extras.get("data")));

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void textRecognizer(Uri imageUri) {

        try {
            InputImage image = InputImage.fromFilePath(getContext(), imageUri);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    String resultText = visionText.getText();
                                    Log.d("textRecognizionBlock",resultText.matches("^\\d{8}[A-Z]{1}$")+""+ resultText );

                                        boolean encontrado = false;
                                        String dni_foto = "";
                                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                                            String blockText = block.getText();
                                            Log.d("textRecognizionBlock", blockText);

                                            for (Text.Line line : block.getLines()) {
                                                String lineText = line.getText();
                                                Log.d("textRecognizionLine", lineText);
                                                Log.d("textRecognizionLine", lineText.trim().matches("^\\d{8}[A-Z]{1}$")+"");

                                                if (lineText.trim().matches("^\\d{8}[A-Z]{1}$") || lineText.trim().matches("^.+?\\d{8}[A-Z]{1}+$")) {
                                                    if(lineText.contains(" ")){
                                                        dni_foto = lineText.trim().substring(lineText.indexOf(" "), lineText.length());
                                                    }else{
                                                        dni_foto = lineText.trim();
                                                    }
                                                    Log.d("textRecognizionLine","ENCONTRADOO"+dni_foto);

                                                    encontrado = true;
                                                    break;
                                                }
                                            }

                                            if (encontrado) {
                                                break;
                                            }

                                        }

                                        if (u.getDni().equalsIgnoreCase("")) {
                                            Toast.makeText(getContext(), "Dni añadido Con éxito", Toast.LENGTH_SHORT).show();
                                            u.setDniVerified(true);
                                            u.setDni(dni_foto);
                                            mListener.getFirestoreInstance().updateUser(u);
                                            mListener.setUserInformation(u);
                                        } else {
                                            if (dni_foto.trim().equalsIgnoreCase(u.getDni())) {
                                                Toast.makeText(getContext(), "Dni Validado Con éxito", Toast.LENGTH_SHORT).show();
                                                u.setDniVerified(true);
                                                mListener.getFirestoreInstance().updateUser(u);
                                            } else {
                                                Toast.makeText(getContext(), "EL Dni no se corresponde con los datos personales guardados o la foto no tiene la claridad necesaria, prueba de nuevo", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "error with textRecognizion", Toast.LENGTH_SHORT).show();
                                        }
                                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}