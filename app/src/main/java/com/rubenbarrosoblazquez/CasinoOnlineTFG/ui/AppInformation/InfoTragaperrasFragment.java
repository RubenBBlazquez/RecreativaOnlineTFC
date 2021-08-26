package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.AppInformation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

public class InfoTragaperrasFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_info_tragaperras, container, false);

        return root;
    }
}