package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import android.view.View;

import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;

import java.util.List;

public class RuletaViewModel extends ViewModel {

    private MutableLiveData<List<Apuesta>> apuestas;
    private MutableLiveData<List<Apuesta>> apuestaActual;
    private MutableLiveData<Long> timerActual;
    private MutableLiveData<List<Integer>> recentNumbersGrid;

    public RuletaViewModel() {
        apuestas = new MutableLiveData<>();
        apuestaActual = new MutableLiveData<>();
        timerActual=new MutableLiveData<>();
        recentNumbersGrid = new MutableLiveData<>();
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

    public void setRecentNumbersGrid(List<Integer> recentNumbersGrid){
        this.recentNumbersGrid.setValue(recentNumbersGrid);
    }

    public MutableLiveData<List<Integer>> getRecentNumbersGrid() {
        return recentNumbersGrid;
    }


}