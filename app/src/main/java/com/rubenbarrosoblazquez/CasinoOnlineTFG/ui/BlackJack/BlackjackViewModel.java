package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlackjackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BlackjackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}