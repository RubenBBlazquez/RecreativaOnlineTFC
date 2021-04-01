package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.List;


public class MyUsersAdminRecyclerViewAdapter extends RecyclerView.Adapter<MyUsersAdminRecyclerViewAdapter.ViewHolder>{

    private final List<User> mValues;

    public MyUsersAdminRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usuario_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.email.setText(holder.mItem.getEmail()+"");
        holder.email.setBackgroundColor(Color.WHITE);
        holder.saldo.setText(holder.mItem.getSaldo()+"");
        holder.saldo.setBackgroundColor(Color.WHITE);
        holder.saldo_gastado.setText(holder.mItem.getSaldo_gastado()+"");
        holder.saldo_gastado.setBackgroundColor(Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.check=!holder.check;
                if(holder.check){
                    holder.container.setBackgroundColor(Color.BLACK);
                }else{
                    holder.container.setBackgroundColor(Color.WHITE);
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
        public final TextView email;
        public final TextView saldo;
        public final TextView saldo_gastado;
        public boolean check;
        public User mItem;
        public LinearLayout container;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.email=view.findViewById(R.id.emailUsuarioItem);
            this.saldo=view.findViewById(R.id.saldoUserItem);
            this.saldo_gastado=view.findViewById(R.id.saldoGastadoUserItem);
            container=view.findViewById(R.id.layoutUserItem);
            check=false;

        }
    }
}