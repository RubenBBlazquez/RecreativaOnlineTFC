 package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MyCardsRecyclerViewAdapter extends RecyclerView.Adapter<MyCardsRecyclerViewAdapter.ViewHolder>{

    private final List<Integer> mValues;
    public int picas[] = new int[]{R.drawable.picas1,R.drawable.picas2,R.drawable.picas3,R.drawable.picas4,R.drawable.picas5,R.drawable.picas6,R.drawable.picas7,R.drawable.picas8,R.drawable.picas9,R.drawable.picas10,R.drawable.picas11,R.drawable.picas12,R.drawable.picas13};
    public int treboles[] = new int[]{R.drawable.trebol1,R.drawable.trebol2,R.drawable.trebol3,R.drawable.trebol4,R.drawable.trebol5,R.drawable.trebol6,R.drawable.trebol7,R.drawable.trebol8,R.drawable.trebol9,R.drawable.trebol10,R.drawable.trebol11,R.drawable.trebol12,R.drawable.trebol13};
    public int rombos[] = new int[]{R.drawable.rombo1,R.drawable.rombo2,R.drawable.rombo3,R.drawable.rombo4,R.drawable.rombo5,R.drawable.rombo6,R.drawable.rombo7,R.drawable.rombo8,R.drawable.rombo9,R.drawable.rombo10,R.drawable.rombo11,R.drawable.rombo12,R.drawable.rombo13};
    public int corazones[] = new int[]{R.drawable.corazon1,R.drawable.corazon2,R.drawable.corazon3,R.drawable.corazon4,R.drawable.corazon5,R.drawable.corazon6,R.drawable.corazon7,R.drawable.corazon8,R.drawable.corazon9,R.drawable.corazon10,R.drawable.corazon11,R.drawable.corazon12,R.drawable.corazon13};

    private BlackjackFragment mView;
    public int totalPoints = 0;
    public boolean isDealer;
    public MyCardsRecyclerViewAdapter(BlackjackFragment mView,List<Integer> items,boolean isDealer) {
        mValues = items;
        this.mView = mView;
        this.isDealer=isDealer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.cards.setImageResource(holder.mItem);

        holder.cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!holder.isTouched){
                    if(!isDealer){
                        int randomCards = new Random().nextInt(13);

                        int [] cardsArrayToPick = getRandomCardTypeArray();

                        while(isCardAlreadyUsed(cardsArrayToPick[randomCards])){
                            randomCards = new Random().nextInt(13);
                            cardsArrayToPick = getRandomCardTypeArray();
                        }

                        holder.cards.setImageResource(cardsArrayToPick[randomCards]);
                        mValues.set(position,cardsArrayToPick[randomCards]);
                        mView.ExtractedCards.add(cardsArrayToPick[randomCards]);
                        Log.d("blackjackPruebas", Arrays.toString(mView.ExtractedCards.toArray()));
                        holder.isTouched=true;
                        totalPoints += (randomCards+1);
                        mView.points.setText(totalPoints+"");
                    }
                }
            }
        });

    }

    public int[] getRandomCardTypeArray(){
        int randomType = new Random().nextInt(4);
        Log.d("blackjackPruebas",String.valueOf(randomType));

        if (randomType == 0){
            return picas;
        }else if(randomType == 1){
            return treboles;
        }else if(randomType == 2){
            return rombos;
        }else if (randomType == 3){
            return corazones;
        }

        return new int[]{};
    }

    public boolean isCardAlreadyUsed(int randomCard){

        for (Integer card: this.mView.ExtractedCards) {
            if (card == randomCard)
                return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Integer mItem;
        public ImageView cards;
        public boolean isTouched;
        public Integer cartRandomPicked;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cards = view.findViewById(R.id.card);
            isTouched=false;
            cartRandomPicked=null;
        }
    }
}