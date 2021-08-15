package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Games;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile.MyBuysRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InitCasinoFragment extends Fragment {

    private OnAdsListener mAdsListener;

    @BindView(R.id.ruletteCard)
    CardView rulette;
    @BindView(R.id.blackjackCard)
    CardView blackjack;
    @BindView(R.id.slotMachineCard)
    CardView slot_machine;

    private OnGetUserActions actionsListener;

    public InitCasinoFragment() {
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
        View v=inflater.inflate(R.layout.fragment_init_casino, container, false);
        ButterKnife.bind(this,v);
        AdView adView=v.findViewById(R.id.banner_init_page);
        mAdsListener.loadBannerAdView(adView);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        v.findViewById(R.id.initPageContainer).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                actionsListener.showActionBar();

                Handler hideHandler = new Handler();
                hideHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        actionsListener.hideActionBar();
                    }
                },2000);

                return true;
            }
        });

        return v;

    }

    @OnClick(R.id.ruletteCard)
    public void goToRulletePage(View v){
        Navigation.findNavController(v).navigate(R.id.action_nav_init_to_nav_ruleta);
    }
    @OnClick(R.id.slotMachineCard)
    public void goToSlotMachinePage(View v){
        Navigation.findNavController(v).navigate(R.id.action_nav_init_to_nav_tragaperras);
    }
    @OnClick(R.id.blackjackCard)
    public void goToBlackJackPage(View v){
        Navigation.findNavController(v).navigate(R.id.action_nav_init_to_nav_blackjact2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity=(Activity)context;
            this.mAdsListener=(OnAdsListener)activity;
            this.actionsListener = (OnGetUserActions)activity;
        }
    }
}