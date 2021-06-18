package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.TragaPerras;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Arrays;
import java.util.Random;


public class TragaPerrasFragment extends Fragment implements IEventEnd{

    private int resultsMatrix[][]=new int[3][3];
    private ImageViewScrolling imageViewScrollings[];
    private EditText saldoApostado;
    private OnGetUserInformation mListener;
    private User u;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_traga_perras, container, false);


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

        Button b = root.findViewById(R.id.throwRandom);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saldoApostado.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), getContext().getString(R.string.setSlotMachineBet), Toast.LENGTH_SHORT).show();
                }else{
                    image.setValueRandom(100);
                    image2.setValueRandom(100);
                    image3.setValueRandom(100);
                    u.setSaldo_gastado(Float.valueOf(saldoApostado.getText().toString()));
                    u.setSaldo(u.getSaldo()-Float.valueOf(saldoApostado.getText().toString()));
                    mListener.updateBalanceTexts();
                    saldoApostado.setFocusable(false);

                }

            }
        });

        u = mListener.getUserInformation();

        return root;
    }


    @Override
    public void eventEnd(int results[], int count,int tag) {
        Log.d("cositas","tag"+tag);
        if (tag == 1){
            resultsMatrix[0][0] = results[0];
            resultsMatrix[0][1] = results[1];
            resultsMatrix[0][2] = results[2];
        }else if(tag == 2){
            resultsMatrix[1][0] = results[0];
            resultsMatrix[1][1] = results[1];
            resultsMatrix[1][2] = results[2];
        }else if(tag == 3){
            resultsMatrix[2][0] = results[0];
            resultsMatrix[2][1] = results[1];
            resultsMatrix[2][2] = results[2];
        }

        if((!imageViewScrollings[0].isMovement && !imageViewScrollings[1].isMovement && !imageViewScrollings[2].isMovement)){
            Log.d("cositas","2 --"+ Arrays.deepToString(resultsMatrix));
            setPrices();
        }

    }

    public void setPrices() {

        if((resultsMatrix[0][0] >= ImageViewScrolling.BAR[0] && resultsMatrix[0][0] <= ImageViewScrolling.BAR[1])
                && (resultsMatrix[1][0] >= ImageViewScrolling.BAR[0] && resultsMatrix[1][0]<= ImageViewScrolling.BAR[1])
                && (resultsMatrix[2][0] >= ImageViewScrolling.BAR[0] && resultsMatrix[2][0]<= ImageViewScrolling.BAR[1])){
            Log.d("cositas","bar");
            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        } else if((resultsMatrix[0][0] >= ImageViewScrolling.LEMON[0] && resultsMatrix[0][0] <= ImageViewScrolling.LEMON[1])
                && (resultsMatrix[1][0] >= ImageViewScrolling.LEMON[0] && resultsMatrix[1][0]<= ImageViewScrolling.LEMON[1])
                && (resultsMatrix[2][0] >= ImageViewScrolling.LEMON[0] && resultsMatrix[2][0]<= ImageViewScrolling.LEMON[1])){
            Log.d("cositas","lemon");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][0] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[0][0] <= ImageViewScrolling.WATERMELON[1])
                && (resultsMatrix[1][0] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[1][0]<= ImageViewScrolling.WATERMELON[1])
                && (resultsMatrix[2][0] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[2][0]<= ImageViewScrolling.WATERMELON[1])){
            Log.d("cositas","watermelon");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][0] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[0][0] <= ImageViewScrolling.ORANGE[1])
                && (resultsMatrix[1][0] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[1][0]<= ImageViewScrolling.ORANGE[1])
                && (resultsMatrix[2][0] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[2][0]<= ImageViewScrolling.ORANGE[1])){
            Log.d("cositas","orange");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][0] == ImageViewScrolling.TRIPLE[0])
                && (resultsMatrix[1][0] == ImageViewScrolling.TRIPLE[0])
                &&(resultsMatrix[2][0] == ImageViewScrolling.TRIPLE[0])){
            Log.d("cositas","triple");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*10);
        } else if((resultsMatrix[0][0] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[0][0] <= ImageViewScrolling.SEVEN[1])
                && (resultsMatrix[1][0] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[1][0]<= ImageViewScrolling.SEVEN[1])
                && (resultsMatrix[2][0] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[2][0]<= ImageViewScrolling.SEVEN[1])){
            Log.d("cositas","seven");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*6);
        }


        if((resultsMatrix[0][1] >= ImageViewScrolling.BAR[0] && resultsMatrix[0][1] <= ImageViewScrolling.BAR[1])
                && (resultsMatrix[1][1] >= ImageViewScrolling.BAR[0] && resultsMatrix[1][1]<= ImageViewScrolling.BAR[1])
                && (resultsMatrix[2][1] >= ImageViewScrolling.BAR[0] && resultsMatrix[2][1]<= ImageViewScrolling.BAR[1])){
            Log.d("cositas","bar");
            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        } else if((resultsMatrix[0][1] >= ImageViewScrolling.LEMON[0] && resultsMatrix[0][1] <= ImageViewScrolling.LEMON[1])
                && (resultsMatrix[1][1] >= ImageViewScrolling.LEMON[0] && resultsMatrix[1][1]<= ImageViewScrolling.LEMON[1])
                && (resultsMatrix[2][1] >= ImageViewScrolling.LEMON[0] && resultsMatrix[2][1]<= ImageViewScrolling.LEMON[1])){
            Log.d("cositas","lemon");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][1] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[0][1] <= ImageViewScrolling.WATERMELON[1])
                && (resultsMatrix[1][1] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[1][1]<= ImageViewScrolling.WATERMELON[1])
                && (resultsMatrix[2][1] >= ImageViewScrolling.WATERMELON[0] && resultsMatrix[2][1]<= ImageViewScrolling.WATERMELON[1])){
            Log.d("cositas","watermelon");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][1] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[0][1] <= ImageViewScrolling.ORANGE[1])
                && (resultsMatrix[1][1] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[1][1]<= ImageViewScrolling.ORANGE[1])
                && (resultsMatrix[2][1] >= ImageViewScrolling.ORANGE[0] && resultsMatrix[2][1]<= ImageViewScrolling.ORANGE[1])){
            Log.d("cositas","orange");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*3);
        }else if((resultsMatrix[0][1] == ImageViewScrolling.TRIPLE[0])
                && (resultsMatrix[1][1] == ImageViewScrolling.TRIPLE[0])
                &&(resultsMatrix[2][1] == ImageViewScrolling.TRIPLE[0])){
            Log.d("cositas","triple");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*10);
        } else if((resultsMatrix[0][1] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[0][1] <= ImageViewScrolling.SEVEN[1])
                && (resultsMatrix[1][1] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[1][1]<= ImageViewScrolling.SEVEN[1])
                && (resultsMatrix[2][1] >= ImageViewScrolling.SEVEN[0] && resultsMatrix[2][1]<= ImageViewScrolling.SEVEN[1])){
            Log.d("cositas","seven");

            this.u.setSaldo(this.u.getSaldo()+(Integer.parseInt(saldoApostado.getText().toString()))*6);
        }

        Handler mainHandler = new Handler(Looper.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mListener.updateBalanceTexts();
                mListener.UpdateUserInformation(u);
                saldoApostado.setFocusable(true);
            }

        };
        mainHandler.post(myRunnable);
    }
    

        @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CasinoActivity) {
            Activity activity = (Activity) context;
            this.mListener = (OnGetUserInformation) activity;
        }
    }
}