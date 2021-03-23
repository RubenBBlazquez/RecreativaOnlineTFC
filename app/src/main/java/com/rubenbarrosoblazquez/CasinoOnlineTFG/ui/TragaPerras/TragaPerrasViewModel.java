package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.TragaPerras;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TragaPerrasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TragaPerrasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}