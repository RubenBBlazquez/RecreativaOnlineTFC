package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.ApuestasCarreras;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApuestasCarrerasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ApuestasCarrerasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Carreras fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}