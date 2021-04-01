package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.List;
import java.util.Map;


public class MyBidsRecyclerViewAdapter extends RecyclerView.Adapter<MyBidsRecyclerViewAdapter.ViewHolder>{

    private final List<Apuesta> mValues;

    public MyBidsRecyclerViewAdapter(List<Apuesta> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apuesta_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(holder.mItem.getNumero()+"");
        holder.name.setTextColor(Color.WHITE);
        holder.valorApuesta.setText(holder.mItem.getCantidadApostada()+"");
        if (Color.RED==holder.mItem.getColor()) {
            holder.name.setBackgroundColor(Color.RED);
        }else if(Color.BLACK==holder.mItem.getColor()){
            holder.name.setBackgroundColor(Color.BLACK);
        }else{
            holder.name.setBackgroundColor(Color.GREEN);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValues.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView valorApuesta;
        public final Button cancel;
        public Apuesta mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.name=view.findViewById(R.id.numeroApuesta);
            this.valorApuesta=view.findViewById(R.id.valorApuesta);
            this.cancel=view.findViewById(R.id.cancelarApuesta);

        }
    }
}