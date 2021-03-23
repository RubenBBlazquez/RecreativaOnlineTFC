package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RuletaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RuletaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}