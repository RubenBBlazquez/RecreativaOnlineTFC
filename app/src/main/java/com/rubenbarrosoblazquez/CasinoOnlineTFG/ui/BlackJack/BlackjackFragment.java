package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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

import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile.MyBuysRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    MyCardsRecyclerViewAdapter adapter;
    MyCardsRecyclerViewAdapter adapter2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(BlackjackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blackjack, container, false);

        ButterKnife.bind(this,root);

        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        initRecyclerView(root);

        return root;
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

        ArrayList<Integer> backcards = new ArrayList<>();
        backcards.add(R.drawable.back_card);
        backcards.add(R.drawable.back_card);
        backcards.add(R.drawable.back_card);
        backcards.add(R.drawable.back_card);

        adapter=new MyCardsRecyclerViewAdapter(this,backcards,false);
        cartasYou.setAdapter(adapter);
        cartasYou.setItemAnimator(new DefaultItemAnimator());

        adapter2=new MyCardsRecyclerViewAdapter(this,backcards,true);
        cartasDealer.setAdapter(adapter2);
        cartasDealer.setItemAnimator(new DefaultItemAnimator());

    }
}