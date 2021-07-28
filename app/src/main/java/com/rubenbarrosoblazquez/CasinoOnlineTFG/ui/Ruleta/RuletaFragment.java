package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import java.util.DoubleSummaryStatistics;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Apuesta;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Monedas;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.slideshow.SlideshowViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.stream.Collectors.groupingBy;

public class RuletaFragment extends Fragment implements MenuItem.OnMenuItemClickListener, View.OnClickListener {
    private static final String[] sectors = {"32 red", "15 black",
            "19 red", "4 black", "21 red", "2 black", "25 red", "17 black", "34 red",
            "6 black", "27 red", "13 black", "36 red", "11 black", "30 red", "8 black",
            "23 red", "10 black", "5 red", "24 black", "16 red", "33 black",
            "1 red", "20 black", "14 red", "31 black", "9 red", "22 black",
            "18 red", "29 black", "7 red", "28 black", "12 red", "35 black",
            "3 red", "26 black", "0 green"
    };
    private static final String[] sectorsBids = {"1 red", "2 black",
            "3 red", "4 black", "5 red", "6 black", "7 red", "8 black", "9 red"
            , "10 black", "11 red", "12 black", "13 red", "14 black", "15 red", "16 black",
            "17 red", "18 black", "19 red", "20 black", "21 red", "22 black",
            "23 red", "24 black", "25 red", "26 black", "27 red", "28 black",
            "29 red", "30 black", "31 red", "32 black", "33 red", "34 black",
            "35 red", "36 black", "1/3", "2/3", "3/3"
    };

    private static final int[][] tercios = {{1,4,7,10,13,16,19,22,25,28,31,34},{2,5,8,11,14,17,20,23,26,29,32,35},{3,6,9,12,15,18,21,24,27,30,33,36}};

    private static final Monedas[] monedasApuesta = {
            new Monedas("0.20€", false), new Monedas("0.50€", false), new Monedas("1€", false), new Monedas("2€", false), new Monedas("5€", false)
    };

    private ImageView wheel;
    private ImageView arrow;
    private String numero_sacado;
    private WheelMotionAsync motionWheel;
    private TextView chrono;
    private androidx.gridlayout.widget.GridLayout recentNumbers;
    private AlertDialog dialog;
    private AlertDialog dialogNumeroSacado;
    private AlertDialog dialogApuestasExtendidas;
    private OnAdsListener mAdsListener;
    private androidx.gridlayout.widget.GridLayout monedasApuestasgrid;
    private androidx.gridlayout.widget.GridLayout numerosApuestas;
    private OnGetUserInformation userListener;
    private ArrayList<Apuesta> apuestaActual;
    private ArrayList<Apuesta> apuestaSumaTotales;
    private TextView saldo;
    private TextView cantidadApostada;

    private boolean launchReady=true;
    private boolean isTheFragmentStart = true;

    private RuletaViewModel ruleta;

    private MyBidsRecyclerViewAdapter adapterApuestas;

    @BindView(R.id.launchRulette)
    ImageButton launchRulette;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ruleta, container, false);
        Log.d("lyfeCycle","creo vista");
        ruleta = new ViewModelProvider(this).get(RuletaViewModel.class);

        ButterKnife.bind(this,root);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        this.apuestaSumaTotales = new ArrayList<>();

        this.motionWheel = new WheelMotionAsync();

        this.wheel = root.findViewById(R.id.Wheel);

        this.arrow = root.findViewById(R.id.ArrowWheel);

        this.chrono = root.findViewById(R.id.chronometerWheel);

        this.recentNumbers = root.findViewById(R.id.recentNumbers);

        this.numero_sacado = "";

        this.apuestaActual = new ArrayList<>();

        this.cantidadApostada = root.findViewById(R.id.apuestaactualRuleta);

        ImageView apuestasExtendido = root.findViewById(R.id.apuestasExtendido);
        apuestasExtendido.setOnClickListener(this);

        setHasOptionsMenu(true);

        this.launchRulette.setOnClickListener(this);

        if(userListener.getUserInformation().isDniVerified()){
            root.findViewById(R.id.dniNoValidatedLayer).setVisibility(View.GONE);
        }

        ruleta.getApuestaActual().observe(getViewLifecycleOwner(), new Observer<List<Apuesta>>() {
            @Override
            public void onChanged(List<Apuesta> apuestas) {
                apuestaActual = (ArrayList<Apuesta>) apuestas;
                cantidadApostada.setText(String.valueOf(getTotalApuesta()));
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(userListener.getUserInformation().isDniVerified()) {
            try{
                motionWheel.execute();

                ruleta = new ViewModelProvider(this).get(RuletaViewModel.class);


            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    @Override
    public void onStop() {
        super.onStop();
        if(userListener.getUserInformation().isDniVerified()) {
            motionWheel.salir = true;
            motionWheel.cancel(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ruleta_menu, menu);
        MenuItem item = menu.findItem(R.id.apuestaRuleta);
        item.setOnMenuItemClickListener(this);

    }

    private void dialogoApostar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.dialog_apuesta_ruleta, null);
        numerosApuestas = v.findViewById(R.id.numerosApuestas);

        saldo = v.findViewById(R.id.saldoActualDialogoApuesta);
        saldo.setText(String.valueOf(userListener.getUserInformation().getSaldo()));

        monedasApuestasgrid = v.findViewById(R.id.monedasApuestas);

        Button apostar = v.findViewById(R.id.apostarRuleta);
        apostar.setOnClickListener(this);

        Button cerrarDialogo = v.findViewById(R.id.cerrarDialogoApuesta);
        cerrarDialogo.setOnClickListener(this);

        AdView adView = v.findViewById(R.id.banner_apuesta_ruleta);
        this.mAdsListener.loadBannerAdView(adView);

        this.pintarNumerosApuestas(numerosApuestas, v);
        this.pintarGridMonedasApuestas(monedasApuestasgrid);


        builder.setView(v);
        builder.create();
        dialog = builder.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dialogoApuestasExtendida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.dialog_apuestas_extendido, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerApuestasExtendido);
        recyclerView.setLayoutManager(linearLayoutManager);


        Map<String, List<Apuesta>> apuestas = apuestaActual.stream().collect(groupingBy(Apuesta::getNumero));
        apuestas.forEach((k, valor) -> {
            DoubleSummaryStatistics suma = valor.stream().collect(Collectors.summarizingDouble(p -> (p.getCantidadApostada())));
            apuestaSumaTotales.add(new Apuesta(k, suma.getSum(), valor.stream().findFirst().get().getColor()));
        });

        ruleta.setApuestas(apuestaSumaTotales);

        adapterApuestas = new MyBidsRecyclerViewAdapter(apuestaSumaTotales);
        recyclerView.setAdapter(adapterApuestas);


        Button cerrar = v.findViewById(R.id.cerrarDialogoApuestaExtendida);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidadApostada.setText(getTotalApuesta() + "");
                dialogApuestasExtendidas.dismiss();
            }
        });

        // this.apuestaActual.forEach(e-> Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show());

        builder.setView(v);
        builder.create();
        dialogApuestasExtendidas = builder.show();

    }

    private void pintarNumerosApuestas(androidx.gridlayout.widget.GridLayout numerosApuestas, View v) {
        Button zeroGreen = v.findViewById(R.id.zeroButton);
        zeroGreen.setTag("0");
        zeroGreen.setOnClickListener(this);

        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        for (int i = 0; i < sectorsBids.length; i++) {
            param = new GridLayout.LayoutParams();
            Button boton = new Button(getContext());
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);

            if (sectorsBids[i].contains(" ")) {
                String sector[] = sectorsBids[i].split(" ");
                boton.setText(sector[0]);
                boton.setTag(sector[0]);
                boton.setWidth(80);
                Drawable color;
                int colorLetter = 0;
                int span = 0;

                if (sector[1].equalsIgnoreCase("red")) {
                    color = ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_red, null);
                    colorLetter = Color.BLACK;
                    span = 1;

                } else if (sector[1].equalsIgnoreCase("black")) {
                    color = ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_black, null);
                    colorLetter = Color.WHITE;
                    span = 1;

                } else {
                    color = ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_green, null);
                    colorLetter = Color.BLACK;
                    span = 3;
                }

                boton.setBackground(color);
                boton.setTextColor(colorLetter);
                param.columnSpec = GridLayout.spec(0, span);
                boton.setLayoutParams(param);
            } else {
                boton.setText(sectorsBids[i]);
                boton.setTag(sectorsBids[i]);
                boton.setWidth(80);
                boton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_green, null));
                boton.setTextColor(Color.BLACK);
            }

            boton.setOnClickListener(this);
            numerosApuestas.addView(boton);

        }
    }

    private void pintarGridMonedasApuestas(androidx.gridlayout.widget.GridLayout monedasApuestasgrid) {
        monedasApuestasgrid.removeAllViews();
        for (int i = 0; i < monedasApuesta.length; i++) {
            final Button boton = new Button(getContext());
            boton.setTag(monedasApuesta[i].getValor());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(140, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart(10);
            boton.setLayoutParams(layoutParams);
            boton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.monedas_shape, null));
            boton.setText(monedasApuesta[i].getValor());
            boton.setOnClickListener(this);
            monedasApuestasgrid.addView(boton);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.apuestaRuleta:
                if (!chrono.getText().toString().equalsIgnoreCase("00:00")) {
                    this.dialogoApostar();
                    break;
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_se_puede_apostar), Toast.LENGTH_SHORT).show();
                }

        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cerrarDialogoApuesta:
                dialog.dismiss();
                break;
            case R.id.apostarRuleta:
                this.userListener.getFirestoreInstance().updateSaldo(this.userListener.getUserInformation().getEmail(), Float.valueOf(this.saldo.getText().toString()));
                this.userListener.getUserInformation().setSaldo(Float.valueOf(this.saldo.getText().toString()));

                this.cantidadApostada.setText(String.valueOf(this.getTotalApuesta()));

                this.userListener.getUserInformation().setSaldo_gastado((float) (this.userListener.getUserInformation().getSaldo_gastado()+getTotalApuesta()));
                this.userListener.getFirestoreInstance().updateSaldoGastado(this.userListener.getUserInformation().getEmail(), this.userListener.getUserInformation().getSaldo_gastado());

                this.userListener.updateBalanceTexts();

                dialog.dismiss();
                break;
            case R.id.apuestasExtendido:
                this.dialogoApuestasExtendida();
                break;
            case R.id.launchRulette:

                if(launchReady){
                    if(motionWheel.chronometer != null){
                        motionWheel.chronometer.onFinish();
                        motionWheel.launch=true;
                        launchReady=false;
                    }else{
                        Toast.makeText(this.getContext(), getString(R.string.launchRuletteReady), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this.getContext(), getString(R.string.launchRuletteReady), Toast.LENGTH_SHORT).show();
                }

        }


        if (view.getTag() != null) {
            //        "0.20","0.50","1","2","5"
            int index = 0;
            int indexApuesta = 0;
            boolean pulsado = false;
            boolean pulsadoApuesta = false;

            switch (String.valueOf(view.getTag())) {
                case "0.20€":
                    index = 0;
                    pulsado = true;
                    break;
                case "0.50€":
                    index = 1;
                    pulsado = true;
                    break;
                case "1€":
                    index = 2;
                    pulsado = true;
                    break;
                case "2€":
                    index = 3;
                    pulsado = true;
                    break;
                case "5€":
                    index = 4;
                    pulsado = true;
                    break;
                default:
                    if (checkAlgunaMonedaPulsada()) {
                        if (Double.valueOf(this.saldo.getText().toString()) > getMonedaActualPulsada()) {
                            for (int i = 0; i < this.numerosApuestas.getChildCount(); i++) {
                                Button b = (Button) this.numerosApuestas.getChildAt(i);
                                if (b.getText().equals(String.valueOf(view.getTag()))) {
                                    if (b.getBackground().getConstantState().equals(ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_black, null).getConstantState()) ||
                                            b.getBackground().getConstantState() == (ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_black_pulsado, null).getConstantState())) {

                                        this.numerosApuestas.getChildAt(i).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_black_pulsado, null));
                                        this.apuestaActual.add(new Apuesta(b.getText().toString(), getMonedaActualPulsada(), Color.BLACK));
                                        this.saldo.setText(Double.valueOf(this.saldo.getText().toString()) - getMonedaActualPulsada() + "");

                                    } else if (b.getBackground().getConstantState() == (ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_red, null).getConstantState()) || b.getBackground().getConstantState() == (ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_red_pulsado, null).getConstantState())) {

                                        this.numerosApuestas.getChildAt(i).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_red_pulsado, null));
                                        this.apuestaActual.add(new Apuesta(b.getText().toString(), getMonedaActualPulsada(), Color.RED));
                                        this.saldo.setText(Double.valueOf(this.saldo.getText().toString()) - getMonedaActualPulsada() + "");
                                    }else{
                                        this.numerosApuestas.getChildAt(i).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_bid_number_green_pulsado, null));
                                        this.apuestaActual.add(new Apuesta(b.getText().toString(), getMonedaActualPulsada(), Color.GREEN));
                                        this.saldo.setText(Double.valueOf(this.saldo.getText().toString()) - getMonedaActualPulsada() + "");
                                    }

                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.noSaldoParaApostar), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.seleccionaMoneda), Toast.LENGTH_SHORT).show();
                    }

            }

            if (pulsado) {
                monedasApuesta[index].pulsado = true;
                Button boton = (Button) monedasApuestasgrid.getChildAt(index);
                boton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.monedas_shape_pulsada, null));
                despulsarMonedas(index);
            }

            this.ruleta.setApuestaActual(apuestaActual);

        }
    }

    private void despulsarMonedas(int index) {
        for (int i = 0; i < monedasApuesta.length; i++) {
            if (i != index) {
                monedasApuesta[i].pulsado = false;
                monedasApuestasgrid.getChildAt(i).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.monedas_shape, null));
            }
        }
    }

    private boolean checkAlgunaMonedaPulsada() {
        for (int i = 0; i < monedasApuesta.length; i++) {
            if (monedasApuesta[i].pulsado) {
                return true;
            }
        }

        return false;
    }

    private double getMonedaActualPulsada() {
        for (int i = 0; i < monedasApuesta.length; i++) {
            if (monedasApuesta[i].pulsado) {
                return Double.valueOf(monedasApuesta[i].getValor().split("€")[0]);
            }
        }

        return 0.0;
    }

    private double getTotalApuesta() {
        double total = 0.0;
        for (Apuesta a : this.apuestaActual) {
            total += a.getCantidadApostada();
        }

        return total;
    }


    private Double getDineroGanado(String numero_sacado) {
        double dineroGanado = 0.0;
        try{
            int numero = Integer.parseInt(numero_sacado.split(" ")[0]);

            for (Apuesta a : apuestaActual) {
                int number=0;
                if(!a.getNumero().contains("/")){
                    number=Integer.parseInt(a.getNumero());
                    if (numero == number) {
                        dineroGanado += a.getCantidadApostada() * 36;
                    }

                }else{
                    number=Integer.parseInt(a.getNumero().substring(0,a.getNumero().indexOf("/")));

                    if(isNumeroInTercio(number,numero)){
                        dineroGanado += a.getCantidadApostada() * 2;
                    }

                }


            }

            userListener.getUserInformation().setSaldo((float) (userListener.getUserInformation().getSaldo() + dineroGanado));
            userListener.getFirestoreInstance().updateSaldo(userListener.getUserInformation().getEmail(), userListener.getUserInformation().getSaldo());
            userListener.updateBalanceTexts();
        }catch (Exception e){

        }


        return dineroGanado;
    }


    public boolean isNumeroInTercio(int tercio,int numeroSacado){
        for (int i = 0; i < this.tercios[0].length ; i++) {
            if(this.tercios[tercio-1][i]==numeroSacado){
                return true;
            }

        }

        return false;
    }


    public class WheelMotionAsync extends AsyncTask<String, String, String> {
        public boolean salir = false;
        private int degree = 0, degreeOld = 0;
        Timer chronometer ;
        private AlertDialog.Builder dialogNumber;
        private boolean animationEnd = true;
        boolean launch = true;
        @Override
        protected String doInBackground(String... strings) {

            while (!salir) {

                Log.d("cositas","animationEnd : "+animationEnd);

                if(chrono.getText().equals("00:00") && launch && !isTheFragmentStart) {
                    spinWheelWithResult();
                    launch=false;
                    animationEnd=true;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (animationEnd){
                    publishProgress();
                    animationEnd=false;
                    launchReady=true;
                    launch=true;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return strings[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            
            chronometer = (Timer) new Timer(30000, 1000).start();
        }

        public void spinWheelWithResult() {
            degreeOld = degree % 360;
            // we calculate random angle for rotation of our wheel
            Random r = new Random();
            degree = r.nextInt(360) + 720;
            // rotation effect on the center of the wheel
            RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(7200);
            rotateAnim.setFillAfter(true);
            rotateAnim.setInterpolator(new DecelerateInterpolator());
            rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // we empty the result text view when the animation start
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    dialogNumber = new AlertDialog.Builder(getActivity());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // we display the correct sector pointed by the triangle at the end of the rotate animation
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    View v = inflater.inflate(R.layout.dialog_numero_sacado_ruleta, null);
                    numero_sacado = getSector(360 - (degree % 360));
                    ((TextView) v.findViewById(R.id.textView9)).setText(getString(R.string.numero_sacado) + numero_sacado);
                    ((TextView) v.findViewById(R.id.DineroGanadoDialogoApuesta)).setText(getString(R.string.ganarDinero) + " " + getDineroGanado(numero_sacado));
                    addRecentNumberToGrid(numero_sacado);
                    apuestaActual.clear();
                    dialogNumber.setView(v);
                    dialogNumeroSacado = dialogNumber.show();
                    apuestaActual.clear();
                    cantidadApostada.setText("0");
                    chronometer.onFinish();
                    animationEnd=true;

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            wheel.startAnimation(rotateAnim);
        }

        private static final float HALF_SECTOR = 360f / 37f / 2f;

        private String getSector(int degrees) {
            int i = 0;
            String text = null;

            do {
                float start = HALF_SECTOR * (i * 2 + 1);
                float end = HALF_SECTOR * (i * 2 + 3);

                if (degrees >= start && degrees < end) {
                    text = sectors[i];
                }

                i++;
            } while (text == null && i < sectors.length);

            return text;
        }

        private void addRecentNumberToGrid(String number) {
            if (number != null) {
                if (number.contains(" ")) {

                    String n_with_color[] = number.split(" ");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(210, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button boton = new Button(getActivity());
                    boton.setLayoutParams(layoutParams);
                    boton.setText(n_with_color[0]);
                    boton.setWidth(80);
                    Drawable back = null;
                    int color = 0;
                    int colorLetter = 0;

                    if (n_with_color[1].equalsIgnoreCase("red")) {
                        color = Color.RED;
                        colorLetter = Color.BLACK;
                        back = getActivity().getDrawable(R.drawable.borders_numbers_red_recents_out_rulette);
                    } else if (n_with_color[1].equalsIgnoreCase("black")) {
                        color = Color.BLACK;
                        colorLetter = Color.WHITE;
                        back = getActivity().getDrawable(R.drawable.borders_numbers_recents_out_rulette);
                    } else {
                        color = Color.GREEN;
                        colorLetter = Color.BLACK;
                        back = getActivity().getDrawable(R.drawable.borders_numbers_green_recents_out_rulette);
                    }

                    boton.setBackgroundColor(color);
                    boton.setTextColor(colorLetter);
                    boton.setBackground(back);

                    if (recentNumbers.getChildCount() < 5) {
                        recentNumbers.addView(boton);
                    } else {
                        recentNumbers.removeView(recentNumbers.getChildAt(0));
                        recentNumbers.addView(boton);
                    }
                }
            }
        }
    }

    public class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            // Used for formatting digit to be in 2 digits only
            ruleta.addToTimer(millisUntilFinished);

            NumberFormat f = new DecimalFormat("00");

            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;

            chrono.setText(f.format(min) + ":" + f.format(sec));

            isTheFragmentStart=false;

            if (dialogNumeroSacado != null) {
                dialogNumeroSacado.dismiss();
            }
        }

        // When the task is over it will print 00:00:00 there
        public void onFinish() {
            chrono.setText("00:00");
            if(this!=null){
                this.cancel();
            }
        }



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CasinoActivity) {
            Activity activity = (Activity) context;
            this.mAdsListener = (OnAdsListener) activity;
            this.userListener = (OnGetUserInformation) activity;
        }
    }
}