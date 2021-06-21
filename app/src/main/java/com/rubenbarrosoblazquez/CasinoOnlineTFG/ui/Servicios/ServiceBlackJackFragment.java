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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnProductsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;

public class ServiceBlackJackFragment extends Fragment {

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
        View v = inflater.inflate(R.layout.fragment_service_blackjack, container, false);
        productsPrueba=new ArrayList<>();

        initRecyclerViewServiciosVarios(v);

        return v;
    }



    public void initRecyclerViewServiciosVarios(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.recyclerBlackJack);
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