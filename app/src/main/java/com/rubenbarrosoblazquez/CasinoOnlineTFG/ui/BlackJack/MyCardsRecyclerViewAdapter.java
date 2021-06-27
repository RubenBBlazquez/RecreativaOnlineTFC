package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.List;
import java.util.Random;


public class MyCardsRecyclerViewAdapter extends RecyclerView.Adapter<MyCardsRecyclerViewAdapter.ViewHolder>{

    private final List<Integer> mValues;
    private int picas[] = new int[]{R.drawable.picas1,R.drawable.picas2,R.drawable.picas3,R.drawable.picas4,R.drawable.picas5,R.drawable.picas6,R.drawable.picas7,R.drawable.picas8,R.drawable.picas9,R.drawable.picas10,R.drawable.picas11,R.drawable.picas12,R.drawable.picas13};
    private BlackjackFragment mView;
    private int totalPoints;
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
                    int random = new Random().nextInt(13);
                    holder.cards.setImageResource(picas[random]);
                    mValues.set(position,picas[random]);
                    holder.isTouched=true;
                    totalPoints += (random+1);

                    if(isDealer){
                        mView.pointsDealer.setText("Points : "+ totalPoints);
                    }else{
                        mView.points.setText("Points : "+ totalPoints);
                    }
                }
            }
        });

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