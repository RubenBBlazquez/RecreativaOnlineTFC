package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnProductsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;

public class ServiceGamesFragment extends Fragment {

    private ArrayList<products> productsPrueba;
    private OnProductsListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service_games, container, false);
        productsPrueba=new ArrayList<>();
        // public products(String descripcion, Bitmap DIR_IMG, String nombre, int n_bastidor, double precio, String tipo, int cantidad) {
        productsPrueba.add(new products("buenas tardes", BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on), "product 1", 0, 99.92, "1", 20));
        productsPrueba.add(new products("buenas tardes", BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on), "product 1", 0, 99.92, "1", 20));
        productsPrueba.add(new products("buenas tardes", BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on), "product 1", 0, 99.92, "1", 20));
        productsPrueba.add(new products("buenas tardes", BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on), "product 1", 0, 99.92, "1", 20));
        productsPrueba.add(new products("buenas tardes", BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on), "product 1", 0, 99.92, "1", 20));

        initRecyclerViewServiciosRuleta(v);

        initRecyclerViewServiciosTragaperras(v);

        initRecyclerViewServiciosVarios(v);

        return v;
    }

    public void initRecyclerViewServiciosRuleta(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.recyclerServiciosRuleta);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        recyclerViewProductsShoppingCart.setAdapter(new MyProductsRecyclerViewAdapter(productsPrueba,mListener));


    }

    public void initRecyclerViewServiciosTragaperras(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.recyclerServiciosTragaperras);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        recyclerViewProductsShoppingCart.setAdapter(new MyProductsRecyclerViewAdapter(productsPrueba,mListener));


    }

    public void initRecyclerViewServiciosVarios(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.recyclerServiciosVarios);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        recyclerViewProductsShoppingCart.setAdapter(new MyProductsRecyclerViewAdapter(productsPrueba,mListener));


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            Activity activity=(Activity) context;
            mListener=(OnProductsListener) activity;
        }
    }


}