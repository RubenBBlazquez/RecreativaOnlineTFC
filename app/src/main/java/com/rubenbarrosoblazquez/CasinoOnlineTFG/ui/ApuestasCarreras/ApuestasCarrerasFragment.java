package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.ApuestasCarreras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;


public class ApuestasCarrerasFragment extends Fragment {

    private ApuestasCarrerasViewModel ApuestasCarrerasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ApuestasCarrerasViewModel =
                new ViewModelProvider(this).get(ApuestasCarrerasViewModel.class);

        View root = inflater.inflate(R.layout.fragment_apuestas_carreras, container, false);

        final TextView textView = root.findViewById(R.id.text_carreras);

        ApuestasCarrerasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}