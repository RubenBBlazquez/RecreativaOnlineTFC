package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.TragaPerras;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TragaPerrasFragment extends Fragment implements IEventEnd{

    private int resultsMatrix[][]=new int[3][3];
    private ImageViewScrolling imageViewScrollings[];
    private EditText saldoApostado;
    private OnGetUserInformation mListener;
    private User u;
    private int x;
    private AlertDialog.Builder builder;
    private double saldo_ganado;
    private int contador =0;
    private boolean isSlotMachineWorking;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_traga_perras, container, false);
        builder = new AlertDialog.Builder(getActivity());
        x=1;

        Spinner spinner = root.findViewById(R.id.multiSpinner);
        ArrayList<String> multiplier = new ArrayList<>();
        multiplier.add("x1");
        multiplier.add("x2");
        multiplier.add("x3");
        multiplier.add("x6");
        multiplier.add("x10");

        multiplicatorAdapter adapter = new multiplicatorAdapter(getContext(),R.layout.layout_pais,multiplier);


        spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinner.setAdapter(adapter);


        ImageViewScrolling image = root.findViewById(R.id.scrolling1);
        image.setEventEnd(TragaPerrasFragment.this);
        image.setTag(1);
        ImageViewScrolling image2 = root.findViewById(R.id.scrolling2);
        image2.setEventEnd(TragaPerrasFragment.this);
        image2.setTag(2);
        ImageViewScrolling image3 = root.findViewById(R.id.scrolling3);
        image3.setEventEnd(TragaPerrasFragment.this);
        image3.setTag(3);

        this.imageViewScrollings= new ImageViewScrolling[]{image,image2,image3};

        saldoApostado = (EditText) root.findViewById(R.id.saldoSlotMachine);

        u = mListener.getUserInformation();

        Button b = root.findViewById(R.id.throwRandom);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saldoApostado.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), getContext().getString(R.string.setSlotMachineBet), Toast.LENGTH_SHORT).show();
                }else{

                    if(!isSlotMachineWorking){

                        if(u.getSaldo() > Integer.parseInt(saldoApostado.getText().toString())){
                            image.setValueRandom(100);
                            image2.setValueRandom(100);
                            image3.setValueRandom(100);
                            u.setSaldo_gastado(u.getSaldo_gastado()+Float.valueOf(saldoApostado.getText().toString()));
                            u.setSaldo(u.getSaldo()-Float.valueOf(saldoApostado.getText().toString()));
                            mListener.updateBalanceTexts();
                            saldoApostado.setEnabled(false);
                            contador=0;
                            saldo_ganado=0;
                            isSlotMachineWorking=true;
                        }else{
                            Toast.makeText(getContext(), getContext().getString(R.string.noSaldoParaApostar), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getContext(), getContext().getString(R.string.tiradasSeguidas), Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


        return root;
    }


    @Override
    public void eventEnd(int results[], int count,int tag) {
        Log.d("cositas","tag"+tag);
        boolean booleanWins[]=new boolean[3];
        if (tag == 1){
            resultsMatrix[0][0] = results[0];
            resultsMatrix[0][1] = results[1];
            resultsMatrix[0][2] = results[2];
            booleanWins[0]=areThreeTheSame(resultsMatrix[0]);

        }else if(tag == 2){
            resultsMatrix[1][0] = results[0];
            resultsMatrix[1][1] = results[1];
            resultsMatrix[1][2] = results[2];
            booleanWins[1]=areThreeTheSame(resultsMatrix[1]);

        }else if(tag == 3){
            resultsMatrix[2][0] = results[0];
            resultsMatrix[2][1] = results[1];
            resultsMatrix[2][2] = results[2];
            booleanWins[2]=areThreeTheSame(resultsMatrix[2]);

        }

        if((!imageViewScrollings[0].isMovement && !imageViewScrollings[1].isMovement && !imageViewScrollings[2].isMovement)){

            if(booleanWins[0] && booleanWins[1] && booleanWins[2]){
                setPrices(true);
            }else{
                setPrices(false);
            }


        }

    }



    public boolean areThreeTheSame(int[] results){
        String pic = "";
        String aux = "";

        for (int i = 0; i < results.length; i++) {

                if((results[i] >= ImageViewScrolling.BAR[0] && results[i] <= ImageViewScrolling.BAR[1])){
                    pic = "BAR";
                }else if((results[i] >= ImageViewScrolling.LEMON[0] && results[i] <= ImageViewScrolling.LEMON[1])){
                    pic = "LEMON";
                }else if((results[i] >= ImageViewScrolling.WATERMELON[0] && results[i] <= ImageViewScrolling.WATERMELON[1])){
                    pic = "WATERMELON";
                }else if((results[i] >= ImageViewScrolling.ORANGE[0] && results[i] <= ImageViewScrolling.ORANGE[1])){
                    pic = "ORANGE";
                }else if((results[i] >= ImageViewScrolling.TRIPLE[0])){
                    pic = "TRIPLE";
                }else if((results[i] >= ImageViewScrolling.SEVEN[0] && results[i] <= ImageViewScrolling.SEVEN[1])){
                    pic = "SEVEN";
                }

                if(!pic.equalsIgnoreCase(aux) && i>0){
                    return false;
                }

                aux=pic;
            }

        if(updateUserBalance(pic)){
            return true;
        }else{
            return false;
        }

    }

    public boolean updateUserBalance(String pic){
        if(pic.equalsIgnoreCase("BAR")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(2*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(2*x);
            return true;

        }else if(pic.equalsIgnoreCase("LEMON")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(2*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(2*x);
            return true;

        }else if(pic.equalsIgnoreCase("WATERMELON")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(2*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(2*x);
            return true;

        }else if(pic.equalsIgnoreCase("ORANGE")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(2*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(2*x);
            return true;

        }else if(pic.equalsIgnoreCase("TRIPLE")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(10*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(10*x);
            return true;

        }else if(pic.equalsIgnoreCase("SEVEN")){

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*(6*x));
            this.saldo_ganado=this.saldo_ganado+Double.parseDouble(saldoApostado.getText().toString())*(6*x);
            return true;
        }

        return false;
    }

    public boolean setPrices(boolean isAllTheSame) {

        if(!isAllTheSame){

            areThreeTheSame(new int[]{resultsMatrix[0][0],resultsMatrix[1][0],resultsMatrix[2][0]});
            areThreeTheSame(new int[]{resultsMatrix[0][1],resultsMatrix[1][1],resultsMatrix[2][1]});
            areThreeTheSame(new int[]{resultsMatrix[0][2],resultsMatrix[1][2],resultsMatrix[2][2]});

        }else{
            saldo_ganado= (Integer.parseInt(saldoApostado.getText().toString()))*(15*x);
            this.u.setSaldo((float) (this.u.getSaldo()+saldo_ganado));
        }


        if(contador==0){
            Handler mainHandler = new Handler(Looper.getMainLooper());

            double finalSaldo_ganado = saldo_ganado;
            Runnable myRunnable = () -> {
                mListener.updateBalanceTexts();
                mListener.UpdateUserInformation(u);
                winDialog(String.valueOf(finalSaldo_ganado));
                isSlotMachineWorking=false;
            };

            mainHandler.post(myRunnable);
        }

        contador ++;

        return true;
    }
    

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CasinoActivity) {
            Activity activity = (Activity) context;
            this.mListener = (OnGetUserInformation) activity;
        }
    }


    public void winDialog(String saldoGanado){
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_numero_sacado_ruleta,null);
        TextView dineroGanado = v.findViewById(R.id.DineroGanadoDialogoApuesta);
        TextView titulo = v.findViewById(R.id.textView9);
        titulo.setText("Has Ganado .. :");
        dineroGanado.setText(saldoGanado+" €");
        builder.setView(v);
        builder.create();
        builder.show();
        saldoApostado.setEnabled(true);
    }


    public class multiplicatorAdapter extends ArrayAdapter<String>{

        ArrayList<String> multiplicators;

        public multiplicatorAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.multiplicators= (ArrayList<String>) objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v =  convertView;

            if(v == null){
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView multiplicator = v.findViewById(R.id.multiplier);
            multiplicator.setText(multiplicators.get(position));
            x= Integer.parseInt(multiplicators.get(position).replace("x",""));

            return v;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v =  convertView;

            if(v == null){
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView multiplicator = v.findViewById(R.id.multiplier);
            multiplicator.setText(multiplicators.get(position));

            return v;
        }
    }
}