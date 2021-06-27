package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;

import java.util.List;

public class RuletaViewModel extends ViewModel {

    private MutableLiveData<List<Apuesta>> apuestas;
    private MutableLiveData<List<Apuesta>> apuestaActual;
    private MutableLiveData<Long> timerActual;

    public RuletaViewModel() {
        apuestas = new MutableLiveData<>();
        apuestaActual = new MutableLiveData<>();
        timerActual=new MutableLiveData<>();
    }

    public MutableLiveData<List<Apuesta>> getApuestas() {
        return apuestas;
    }

    public void setApuestas(List<Apuesta> apuestas) {
        this.apuestas.setValue(apuestas);
    }

    public MutableLiveData<List<Apuesta>> getApuestaActual() {
        return apuestaActual;
    }

    public void setApuestaActual(List<Apuesta> apuestaActual) {
        this.apuestaActual.setValue(apuestaActual);
    }

    public MutableLiveData<Long> getTimerActual() {
        return timerActual;
    }

    public void addToTimer(Long timer){
        this.timerActual.setValue(timer);
    }
}