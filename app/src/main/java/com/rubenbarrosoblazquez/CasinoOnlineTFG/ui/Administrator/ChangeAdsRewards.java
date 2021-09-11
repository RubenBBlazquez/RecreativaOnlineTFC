package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.AdReward;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeAdsRewards extends Fragment {

    private OnGetUserActions mListener;

    @BindView(R.id.adRewardInputLayout)
    TextInputEditText rewardEditText;

    @BindView(R.id.changeAdReward)
    Button cambiarRecompensa;

    public ChangeAdsRewards() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_ads_rewards, container, false);

        ButterKnife.bind(this,v);

        this.rewardEditText.setText(String.valueOf(mListener.getAdReward()));

        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.changeAdReward)
    public void changeAdReward(View v){
        this.mListener.getFirestoreRealTimeInstance().setAdReward(new AdReward("RewardedAd",Double.parseDouble(Objects.requireNonNull(this.rewardEditText.getText()).toString())));
        Toast.makeText(getContext(), "Recompensa de anuncios actualizada correctamente", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CasinoActivity){
            Activity activity = (Activity)context;
            this.mListener = (OnGetUserActions)activity;
        }
    }


}