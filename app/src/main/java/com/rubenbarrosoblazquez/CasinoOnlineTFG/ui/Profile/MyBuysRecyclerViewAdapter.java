package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Compras;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.List;

public class MyBuysRecyclerViewAdapter extends RecyclerView.Adapter<MyBuysRecyclerViewAdapter.ViewHolder> {

    private final List<Compras> mValues;
    public MyBuysRecyclerViewAdapter(List<Compras> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.productImage.setImageBitmap(holder.mItem.getImagen());
        holder.nombre.setText(holder.mItem.getName());
        holder.precio.setText("Precio : \t "+holder.mItem.getPrice()+"€ x "+holder.mItem.getUnidades()+" \t Total : "+(Double.valueOf(holder.mItem.getPrice())*Double.valueOf(holder.mItem.getUnidades()))+"€");
        holder.date.setText(holder.mItem.getDate());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nombre;
        public final TextView precio;
        public final ImageView productImage;
        private final TextView date;
        public Compras mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            productImage=(ImageView) view.findViewById(R.id.imageCompra);
            precio=(TextView) view.findViewById(R.id.PrecioCompra);
            nombre=(TextView)view.findViewById(R.id.nombreProductoCompra);
            date=(TextView)view.findViewById(R.id.fechaCompra);

        }
    }
}