package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.Query;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Compras;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MyBuysRecyclerViewAdapter adapter;
    private ArrayList<Compras> compras;
    private OnGetUserActions mListener;

    @BindView(R.id.filterBuys)
    Spinner filter;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_basket, container, false);

        ButterKnife.bind(this,v);

        compras = new ArrayList<>();

        initRecyclerViewComentarios(v);

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.filtro));
        list.add(getString(R.string.orderByDateDesc));
        list.add(getString(R.string.orderByDateAsc));
        list.add(getString(R.string.orderByName));
        list.add(getString(R.string.orderByPriceAsc));
        list.add(getString(R.string.orderByPriceDesc));

        filterAdapter filteradapter = new filterAdapter(getContext(),R.layout.layout_pais,list);
        filter.setAdapter(filteradapter);

        mListener.getFirestoreInstance().getAllBuysByUser(mListener.getUserInformation(),compras,adapter);


        return v;
    }

    public void initRecyclerViewComentarios(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.rcBuys);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        recyclerViewProductsShoppingCart.scheduleLayoutAnimation();
        adapter=new MyBuysRecyclerViewAdapter(compras);
        recyclerViewProductsShoppingCart.setAdapter(adapter);
        recyclerViewProductsShoppingCart.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            Activity activity=(Activity) context;
            mListener=(OnGetUserActions) activity;

        }
    }

    public void orderBuysBY(String order){
        if(order.equalsIgnoreCase(getString(R.string.orderByPriceAsc))){
            mListener.getFirestoreInstance().getAllBuysOrderByPrice(mListener.getUserInformation(),compras,adapter, Query.Direction.ASCENDING,"price");
        }else if(order.equalsIgnoreCase(getString(R.string.orderByPriceDesc))){
            mListener.getFirestoreInstance().getAllBuysOrderByPrice(mListener.getUserInformation(),compras,adapter, Query.Direction.DESCENDING,"price");
        }else if(order.equalsIgnoreCase(getString(R.string.orderByDateDesc))){
            mListener.getFirestoreInstance().getAllBuysOrderByPrice(mListener.getUserInformation(),compras,adapter, Query.Direction.DESCENDING,"date");
        }else if(order.equalsIgnoreCase(getString(R.string.orderByDateAsc))){
            mListener.getFirestoreInstance().getAllBuysOrderByPrice(mListener.getUserInformation(),compras,adapter, Query.Direction.ASCENDING,"date");
        }else if(order.equalsIgnoreCase(getString(R.string.orderByName))){
            mListener.getFirestoreInstance().getAllBuysOrderByPrice(mListener.getUserInformation(),compras,adapter, Query.Direction.ASCENDING,"name");
        }else{
            mListener.getFirestoreInstance().getAllBuysByUser(mListener.getUserInformation(),compras,adapter);

        }
    }


    public class filterAdapter extends ArrayAdapter<String> {

        private ArrayList<String> filtros;
        private Context context;

        public filterAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.filtros = (ArrayList<String>) objects;
        }

        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView multiplicator = v.findViewById(R.id.multiplier);
            multiplicator.setBackgroundColor(R.color.colorSecondary);
            multiplicator.setText(filtros.get(position));

            Toast.makeText(context, filtros.get(position), Toast.LENGTH_SHORT).show();
            orderBuysBY(filtros.get(position));
            return v;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView multiplicator = v.findViewById(R.id.multiplier);
            multiplicator.setBackgroundColor(R.color.colorSecondary);
            multiplicator.setText(filtros.get(position));



            return v;

        }



    }

}