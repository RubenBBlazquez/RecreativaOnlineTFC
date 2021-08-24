package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.BlackJack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BlackjackFragment extends Fragment {

    private BlackjackViewModel slideshowViewModel;

    @BindView(R.id.recyclerCardsyou)
    RecyclerView cartasYou;
    @BindView(R.id.recyclerCardsDealer)
    RecyclerView cartasDealer;

    @BindView(R.id.pointsBlackjackYou)
    TextView points;

    @BindView(R.id.pointsBlackjackDealer)
    TextView pointsDealer;

    @BindView(R.id.sacarOtraCarta)
    Button sacarCarta;
    @BindView(R.id.Plantarse)
    Button plantarse;

    @BindView(R.id.dniNoValidatedLayer)
    LinearLayout backgroundError;

    @BindView(R.id.textError)
    TextView textError;

    @BindView(R.id.containerApostarBlackjack)
    RelativeLayout apostar;

    ArrayList<Integer> cardsYou = new ArrayList<>();
    ArrayList<Integer> cardsDealer = new ArrayList<>();
    ArrayList<Integer> ExtractedCards = new ArrayList<>();

    private boolean hayApuesta = false;

    private OnGetUserActions mListener;

    MyCardsRecyclerViewAdapter adapter;
    MyCardsRecyclerViewAdapter adapter2;

    private AlertDialog dialog;

    private User u;
    private boolean isYouGiveUp = false;
    private double saldoApostado=0;
    private double saldoGanado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(BlackjackViewModel.class);

        View root = inflater.inflate(R.layout.fragment_blackjack, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        ButterKnife.bind(this,root);

        u = mListener.getUserInformation();

        initRecyclerView(root);

        //setHasOptionsMenu(false);

        if(u.isDniVerified()){
            if(hayApuesta){
                backgroundError.setVisibility(View.GONE);
                apostar.setVisibility(View.GONE);
                sacarOtraCarta(false);
                setHasOptionsMenu(false);
                new blackJackAsyncTask().execute("");
            }else{
                backgroundError.setVisibility(View.VISIBLE);
                apostar.setVisibility(View.VISIBLE);
                backgroundError.setBackground(AppCompatResources.getDrawable(this.getContext(), R.drawable.background_black_and_gold));
                textError.setText(getString(R.string.debesApostarBlackjack));
            }
        }else{
            backgroundError.setVisibility(View.VISIBLE);

        }

        root.findViewById(R.id.containerBlackjack).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.showActionBar();

                Handler hideHandler = new Handler();
                hideHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.hideActionBar();
                    }
                },2000);

                return true;            }
        });


        return root;
    }


    public void initRecyclerView(View v) {
        final View view = v;

        GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 4);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 4);

        cartasYou = (RecyclerView) v.findViewById(R.id.recyclerCardsyou);
        cartasYou.setLayoutManager(linearLayoutManager);
        cartasYou.scheduleLayoutAnimation();

        cartasDealer = (RecyclerView) v.findViewById(R.id.recyclerCardsDealer);
        cartasDealer.setLayoutManager(gridLayoutManager);
        cartasDealer.scheduleLayoutAnimation();

        adapter=new MyCardsRecyclerViewAdapter(this,cardsYou,false);
        cartasYou.setAdapter(adapter);
        cartasYou.setItemAnimator(new DefaultItemAnimator());

        adapter2=new MyCardsRecyclerViewAdapter(this,cardsDealer,true);
        cartasDealer.setAdapter(adapter2);
        cartasDealer.setItemAnimator(new DefaultItemAnimator());

    }

    public void sacarOtraCarta(boolean isDealer){
        if(u.isDniVerified()){
            if(isDealer){
                this.cardsDealer.add(R.drawable.back_card);
                adapter2.notifyDataSetChanged();
                Log.d("cositas",Arrays.toString(cardsDealer.toArray()));
            }else{
                if (!isYouGiveUp && hayApuesta) {
                    if (!isThereAnyCartNotTouched()) {
                        this.cardsYou.add(R.drawable.back_card);
                        adapter.notifyDataSetChanged();
                        Log.d("cositas", Arrays.toString(cardsYou.toArray()));
                    } else {
                        Toast.makeText(getContext(), "" + getString(R.string.cardNotTouched), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("cositas",points.getText().toString());
            }
        }

    }

    public boolean isThereAnyCartNotTouched(){
        if (this.cardsYou.size() > 1){
            for (int i = 0; i < this.cardsYou.size() ; i++) {
                if(this.cardsYou.get(i) == R.drawable.back_card){
                    return true;
                }
            }
        }

        return false;
    }


    @OnClick(R.id.sacarOtraCarta)
    public void sacarCartaYou(){
        sacarOtraCarta(false);
    }
    @OnClick(R.id.Plantarse)
    public void plantarse(){
        isYouGiveUp=true;
        Toast.makeText(getContext(), ""+getString(R.string.plantarse), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.apostarBlackjack)
    public void apostar(){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = getLayoutInflater().inflate(R.layout.dialog_apuesta_blackjack, null);

                TextInputEditText apuestaET = v.findViewById(R.id.apuestaBlackjack);

                Button apostar = v.findViewById(R.id.apostarButton);
                apostar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String apuesta = apuestaET.getText().toString();
                        if(!apuesta.isEmpty()){
                            if(Integer.parseInt(apuesta) > u.getSaldo() ){
                                Toast.makeText(getContext(), ""+getString(R.string.noSaldoParaApostar), Toast.LENGTH_SHORT).show();
                            }else if (Integer.parseInt(apuesta) <= 0){
                                Toast.makeText(getContext(), ""+getString(R.string.apuestaCero), Toast.LENGTH_SHORT).show();
                            }else{
                                backgroundError.setVisibility(View.GONE);
                                cardsDealer.clear();
                                cardsYou.clear();
                                adapter.notifyDataSetChanged();
                                adapter2.notifyDataSetChanged();
                                sacarOtraCarta(false);
                                points.setText("0");
                                pointsDealer.setText("0");
                                isYouGiveUp=false;
                                dialog.dismiss();
                                saldoApostado=Integer.parseInt(apuesta);
                                u.setSaldo((float) (u.getSaldo()-saldoApostado));
                                u.setSaldo_gastado((float) ((float)u.getSaldo_gastado()+ saldoApostado));
                                mListener.updateBalanceTexts();
                                hayApuesta = true;
                                new blackJackAsyncTask().execute("");
                            }
                        }else{
                            Toast.makeText(getContext(), ""+getString(R.string.apuestaEmpty), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                Button cerrarDialogo = v.findViewById(R.id.cerrarDialogoBlackjack);
                cerrarDialogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                builder.setView(v);
                builder.create();
                dialog = builder.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity = (Activity)context;
            mListener=(OnGetUserActions)activity;
        }
    }

    public class blackJackAsyncTask extends AsyncTask<Object,Object,Object> {

        public boolean salir = false;

        @Override
        protected Object doInBackground(Object... objects) {
            int totalPoints = 0;
            while (!salir) {
                String message = "";

                int youPoints = Integer.parseInt(points.getText().toString());
                int dealerPoints = Integer.parseInt(pointsDealer.getText().toString());
                if (youPoints <= 21) {
                    if (isYouGiveUp) {

                        if (dealerPoints <= 21 && dealerPoints >= youPoints) {
                            if (dealerPoints == youPoints) {
                                saldoGanado=saldoApostado;
                                message="!EMPATE!";
                                u.setSaldo((float) (u.getSaldo()+saldoApostado));
                                salir = true;
                            } else {
                                saldoGanado=0.0;
                                message="!EL DEALER GANÓ, PRUEBA DE NUEVO!";
                                salir = true;
                            }
                        }else if (dealerPoints >21){
                                salir = true;
                                saldoGanado=saldoApostado*2;
                                u.setSaldo((float)(u.getSaldo()+(saldoApostado*2)));
                                message="!GANASTE EL DEALER SE PASÓ!";
                        }

                        if(!salir){
                            cardsDealer.add(R.drawable.back_card);

                            int randomCards = new Random().nextInt(13);

                            int [] cardsArrayToPick = adapter.getRandomCardTypeArray();

                            while(adapter.isCardAlreadyUsed(cardsArrayToPick[randomCards])){
                                randomCards = new Random().nextInt(13);
                                cardsArrayToPick = adapter.getRandomCardTypeArray();
                            }
                            cardsDealer.set(cardsDealer.size() - 1, adapter.picas[randomCards]);
                            notifyAdapter();
                            totalPoints = dealerPoints + (randomCards + 1);
                            pointsDealer.setText(""+totalPoints);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                }else{
                    salir = true;
                    saldoGanado=0.0;
                    message="PERDISTE, TE PASASTE DE 21!";
                }

                if(salir){

                    String finalSaldoGanado = String.valueOf(saldoGanado);
                    String finalMessage = message;
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.getFirestoreInstance().updateUser(u);
                            mListener.updateBalanceTexts();
                            createPriceDialog(finalSaldoGanado, finalMessage);
                            ExtractedCards.clear();
                        }
                    });


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardsDealer.clear();
                            cardsYou.clear();
                            adapter2.notifyDataSetChanged();
                            cartasDealer.setAdapter(adapter2);
                            adapter.notifyDataSetChanged();
                            adapter.totalPoints=0;
                            cartasYou.setAdapter(adapter);
                            points.setText("0");
                            pointsDealer.setText("0");
                            isYouGiveUp=false;


                        }
                    },1000);
                }

            }

            return objects[0];
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

        }

        public void notifyAdapter(){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter2.notifyDataSetChanged();
                }
            },0);
        }
    }




    public void createPriceDialog(String salgoGanado,String tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_blackjack_prized,null);
        TextView dineroGanado = v.findViewById(R.id.DineroGanadoDialogoApuesta);
        TextView titulo = v.findViewById(R.id.textView9);
        TextView textDealer = v.findViewById(R.id.textoDealer);
        TextView textYou = v.findViewById(R.id.textoYou);
        textDealer.setText("Dealer : "+pointsDealer.getText());
        textYou.setText("You : "+points.getText());
        titulo.setText(tipo);
        dineroGanado.setText(salgoGanado+" €");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);

        RecyclerView dealerCardsDialog = v.findViewById(R.id.dealerDialogCards);
        dealerCardsDialog.setLayoutManager(linearLayoutManager);
        dealerCardsDialog.setAdapter(new MyCardsRecyclerViewAdapter(BlackjackFragment.this,new ArrayList<Integer>(this.cardsDealer),true));

        RecyclerView youCardsDialog = v.findViewById(R.id.youDialogCards);
        youCardsDialog.setLayoutManager(linearLayoutManager2);
        youCardsDialog.setAdapter(new MyCardsRecyclerViewAdapter(BlackjackFragment.this,new ArrayList<Integer>(this.cardsYou),false));

        builder.setView(v);
            builder.create();
            builder.show();

        backgroundError.setVisibility(View.VISIBLE);
        hayApuesta=false;
    }

}