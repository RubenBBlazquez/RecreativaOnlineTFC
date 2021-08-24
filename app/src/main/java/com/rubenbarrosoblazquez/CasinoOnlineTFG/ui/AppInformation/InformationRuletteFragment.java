package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.AppInformation;

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


public class InformationRuletteFragment extends Fragment {

    private InformationRuletteModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(InformationRuletteModel.class);

        View root = inflater.inflate(R.layout.fragment_information_rulette, container, false);

        return root;
    }

}