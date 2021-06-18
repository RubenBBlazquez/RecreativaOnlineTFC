package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnProductsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.List;


public class MyProductsRecyclerViewAdapter extends RecyclerView.Adapter<MyProductsRecyclerViewAdapter.ViewHolder> {

    private final List<products> mValues;
    private OnProductsListener mListener;

    public MyProductsRecyclerViewAdapter(List<products> items, OnProductsListener mListener) {
        mValues = items;
        this.mListener=mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.ProductPrize.setText(String.valueOf(holder.mItem.getPrecio()));
        holder.ProductName.setText(holder.mItem.getNombre());
        holder.mItem.setCantidad(1);
        holder.ProductImage.setImageBitmap(holder.mItem.getImg());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putParcelable("ProductInfo",holder.mItem);
                mListener.goToInfoProduct(view,b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ProductImage;
        public final TextView ProductName;
        public final TextView ProductPrize;
       // public final ImageButton basket;
       // public final ImageButton information;
        public User user;
        public products mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ProductImage=(ImageView)view.findViewById(R.id.imageProducto);
            ProductName=(TextView)view.findViewById(R.id.NombreProducto);
            ProductPrize=(TextView)view.findViewById(R.id.PrecioProducto);
            //basket=(ImageButton) view.findViewById(R.id.cesta);
            //information=(ImageButton) view.findViewById(R.id.infoProd);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + ProductName.getText() + "'"+ProductPrize.getText();
        }
    }
}