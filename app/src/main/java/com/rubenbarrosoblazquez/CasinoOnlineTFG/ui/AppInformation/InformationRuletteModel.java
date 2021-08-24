package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.AppInformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InformationRuletteModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InformationRuletteModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}