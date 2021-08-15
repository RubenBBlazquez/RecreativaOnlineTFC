package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    @BindView(R.id.imageProductAdd)
    ImageView productImage;
    @BindView(R.id.nameProduct)
    TextInputEditText nameProduct;
    @BindView(R.id.descriptionProduct)
    TextInputEditText descriptionProduct;
    @BindView(R.id.priceProduct)
    TextInputEditText priceProduct;
    @BindView(R.id.unidadesProduct)
    TextInputEditText unidadesProduct;

    @BindView(R.id.spinnerTypesAddProducts)
    Spinner types;

    private String tipo;
    private OnGetUserActions mListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_product, container, false);
        ButterKnife.bind(this,v);

        this.productImage.setImageResource(R.drawable.ruleta);

        ArrayList<String> lista = new ArrayList<>();
        lista.add("ruleta");
        lista.add("slot_machine");
        lista.add("blackjack");
        this.types.setAdapter(new typesAdapter(getContext(),R.layout.layout_pais,lista));


        return v;
    }

    @OnClick(R.id.addImageProduct)
    public void addImage(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (pickPhoto.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(pickPhoto, 1);
        }
    }

    @OnClick(R.id.addProduct)
    public void addProduct(){
        //    public products(String descripcion, String nombre, double precio, String tipo, int cantidad) {
        if(validityFields()){
            products p = new products(null,this.descriptionProduct.getText().toString(),this.nameProduct.getText().toString(),Double.valueOf(this.priceProduct.getText().toString()),tipo,Integer.parseInt(unidadesProduct.getText().toString()));
            p.setImg(((BitmapDrawable) this.productImage.getDrawable()).getBitmap());
            mListener.getFirestoreInstance().insertProduct(p);
        }else{
            Toast.makeText(getContext(), getString(R.string.rellenaCampos), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validityFields(){
        int error = 0;

        if(this.descriptionProduct.getText().toString().isEmpty()){
            error = 1;
        }

        if(this.nameProduct.getText().toString().isEmpty()){
            error = 1;
        }

        if(this.priceProduct.getText().toString().isEmpty()){
            error = 1;
        }

        if(this.unidadesProduct.getText().toString().isEmpty()){
            error = 1;
        }

        if(error==1){
            return false;
        }else{
            return true;
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == this.getActivity().RESULT_OK){
            Uri imageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
                this.productImage.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity= (Activity) context;
            this.mListener=(OnGetUserActions)activity;
        }
    }

    public class typesAdapter extends ArrayAdapter<String> {

        ArrayList<String> types;

        public typesAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.types= (ArrayList<String>) objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v =  convertView;

            if(v == null){
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView textViewTypes = v.findViewById(R.id.multiplier);
            textViewTypes.setText(types.get(position));
            tipo= types.get(position);

            return v;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v =  convertView;

            if(v == null){
                v = getLayoutInflater().inflate(R.layout.layout_pais, parent, false);
            }

            TextView textViewTypes = v.findViewById(R.id.multiplier);
            textViewTypes.setText(types.get(position));

            return v;
        }
    }



}