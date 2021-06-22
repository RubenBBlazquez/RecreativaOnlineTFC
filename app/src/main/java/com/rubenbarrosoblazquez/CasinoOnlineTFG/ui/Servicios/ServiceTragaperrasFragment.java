package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnProductsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceTragaperrasFragment extends Fragment {

    private ArrayList<products> products=new ArrayList<>();
    private OnProductsListener mListener;
    private OnGetUserInformation mListenerUser;
    private MyProductsRecyclerViewAdapter adapter;

    @BindView(R.id.serviciosTragaperrasEditProfile)
    TextInputEditText search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service_slot_machine, container, false);
        ButterKnife.bind(this,v);

        initRecyclerViewServiciosTragaperras(v);

        mListenerUser.getFirestoreInstance().getAllProductsByType(products,"slot_machine",adapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListenerUser.getFirestoreInstance().getProductsByName(s.toString(),"slot_machine",adapter,products);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }


    public void initRecyclerViewServiciosTragaperras(View v) {
        final View view = v;

        GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 2);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.recyclerServiciosTragaperras);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        adapter = new MyProductsRecyclerViewAdapter(products,mListener);
        recyclerViewProductsShoppingCart.setAdapter(adapter);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            Activity activity=(Activity) context;
            mListener=(OnProductsListener) activity;
            mListenerUser=(OnGetUserInformation)activity;
        }
    }


}