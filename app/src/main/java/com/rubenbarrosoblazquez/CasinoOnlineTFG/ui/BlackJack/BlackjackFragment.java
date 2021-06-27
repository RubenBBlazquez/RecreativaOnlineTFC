package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile.MyBuysRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BlackjackFragment extends Fragment {

    private BlackjackViewModel slideshowViewModel;

    @BindView(R.id.recyclerCardsyou)
    RecyclerView cartasYou;
    @BindView(R.id.recyclerCardsDealer)
    RecyclerView cartasDealer;

    @BindView(R.id.pointsBlackjackYou)
    TextView points;

    @BindView(R.id.pointsBlackjackDealer)
    TextView pointsDealer;

    @BindView(R.id.sacarOtraCarta)
    Button sacarCarta;
    @BindView(R.id.Plantarse)
    Button plantarse;

    @BindView(R.id.dniNoValidatedLayer)
    LinearLayout backgroundError;

    @BindView(R.id.textError)
    TextView textError;

    ArrayList<Integer> cardsYou = new ArrayList<>();
    ArrayList<Integer> cardsDealer = new ArrayList<>();

    private boolean hayApuesta = false;

    private OnGetUserInformation mListener;

    MyCardsRecyclerViewAdapter adapter;
    MyCardsRecyclerViewAdapter adapter2;

    private User u;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(BlackjackViewModel.class);

        View root = inflater.inflate(R.layout.fragment_blackjack, container, false);

        ButterKnife.bind(this,root);

        u = mListener.getUserInformation();

        if(u.isDniVerified()){
            setHasOptionsMenu(true);
            if(hayApuesta){
                backgroundError.setVisibility(View.GONE);
                initRecyclerView(root);

            }else{
                backgroundError.setVisibility(View.VISIBLE);
                textError.setText(getString(R.string.debesApostarBlackjack));
            }
        }else{
            backgroundError.setVisibility(View.VISIBLE);

        }


        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blackjack_menu, menu);
        MenuItem item = menu.findItem(R.id.apuestaBlackjack);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

    }

    public void initRecyclerView(View v) {
        final View view = v;

        GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 4);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 4);

        cartasYou = (RecyclerView) v.findViewById(R.id.recyclerCardsyou);
        cartasYou.setLayoutManager(linearLayoutManager);
        cartasYou.scheduleLayoutAnimation();

        cartasDealer = (RecyclerView) v.findViewById(R.id.recyclerCardsDealer);
        cartasDealer.setLayoutManager(gridLayoutManager);
        cartasDealer.scheduleLayoutAnimation();

        cardsYou.add(R.drawable.back_card);
        cardsDealer.add(R.drawable.back_card);

        adapter=new MyCardsRecyclerViewAdapter(this,cardsYou,false);
        cartasYou.setAdapter(adapter);
        cartasYou.setItemAnimator(new DefaultItemAnimator());

        adapter2=new MyCardsRecyclerViewAdapter(this,cardsDealer,true);
        cartasDealer.setAdapter(adapter2);
        cartasDealer.setItemAnimator(new DefaultItemAnimator());

    }

    public void sacarOtraCarta(boolean isDealer){
        if(isDealer){
            this.cardsDealer.add(R.drawable.back_card);
            adapter2.notifyDataSetChanged();
            Log.d("cositas",Arrays.toString(cardsDealer.toArray()));
        }else{
            this.cardsYou.add(R.drawable.back_card);
            adapter.notifyDataSetChanged();
            Log.d("cositas",Arrays.toString(cardsYou.toArray()));
        }
    }


    @OnClick(R.id.sacarOtraCarta)
    public void sacarCartaYou(){
        sacarOtraCarta(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity = (Activity)context;
            mListener=(OnGetUserInformation)activity;
        }
    }
}