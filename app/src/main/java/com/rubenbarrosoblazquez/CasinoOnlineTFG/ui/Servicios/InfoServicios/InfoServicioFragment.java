package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.InfoServicios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserActions;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Comments;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.CommentsRealTime;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoServicioFragment extends Fragment {

    private Bundle information;
    private products product;
    private TextView ProductNameInfo;
    private TextView ProductPriceInfo;
    private TextView ProductDecriptionInfo;
    private ImageView ProductImageInfo;
    private NumberPicker numberPicker;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewComments;
    private ProgressDialog progreso;
    private OnGetUserActions mListener;
    private ArrayList<Comments> comments;
    private String url;
    private MyCommentsRecyclerViewAdapter commentAdapter;
    private Button Buy;
    private Button newComment;
    private Activity activity;
    private User u;
    private TextView textNoComments;

    @BindView(R.id.cartAnimation)
    LottieAnimationView cartAnimation;
    @BindView(R.id.cartAnimation2)
    LottieAnimationView cartAnimation2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        information=new Bundle();
        product=new products();

        if (getArguments() != null) {
            this.information=getArguments();
            this.product= (products) information.getParcelable("ProductInfo");
        }
        this.u=mListener.getUserInformation();
        comments = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_info_servicio, container, false);

        ButterKnife.bind(this,v);

        this.ProductDecriptionInfo=(TextView)v.findViewById(R.id.infoDescriptionProduct);
        this.ProductImageInfo=(ImageView)v.findViewById(R.id.ProductImageInfo);
        this.ProductNameInfo=(TextView)v.findViewById(R.id.InfoNameProduct);
        this.ProductPriceInfo=(TextView)v.findViewById(R.id.infoPriceProduct);
        this.numberPicker=(NumberPicker)v.findViewById(R.id.infoUnitsProduct);
        this.Buy=(Button)v.findViewById(R.id.InfoBuyProduct);

        this.Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getSaldo() >= (product.getPrecio()*product.getCantidad())){
                    u.setSaldo((float) (u.getSaldo() - (product.getPrecio()*product.getCantidad())));
                    u.setSaldo_gastado(u.getSaldo_gastado ()+ (float) (product.getPrecio()*product.getCantidad()));
                    mListener.getFirestoreInstance().updateSaldo(u.getEmail(),u.getSaldo());
                    mListener.getFirestoreInstance().updateSaldoGastado(u.getEmail(),u.getSaldo_gastado());
                    mListener.getFirestoreInstance().insertBuy(product,u);
                    mListener.updateBalanceTexts();
                    mListener.setUserInformation(u);
                    Toast.makeText(getContext(), getString(R.string.compraRealizada), Toast.LENGTH_SHORT).show();

                    cartAnimation.setAnimation(R.raw.cart_animation);
                    cartAnimation.playAnimation();
                    cartAnimation2.setAnimation(R.raw.cart_animation);
                    cartAnimation2.playAnimation();

                    final Runnable r = new Runnable() {
                        public void run() {
                           cartAnimation.setImageResource(R.drawable.ic_round_shopping_cart_24);
                           cartAnimation2.setImageResource(R.drawable.ic_round_shopping_cart_24);
                        }
                    };

                    new Handler(Looper.getMainLooper()).postDelayed(r,2000);

                }else{
                    Toast.makeText(getContext(), getString(R.string.noSaldoParaComprar), Toast.LENGTH_SHORT).show();
                }

            }
        });

        this.newComment=(Button)v.findViewById(R.id.addNewComment);
        this.textNoComments=(TextView)v.findViewById(R.id.textNoCommentsYet);

        this.ProductPriceInfo.setText(String.valueOf(this.product.getPrecio())+"$");
        this.ProductNameInfo.setText(String.valueOf(this.product.getNombre()));
        this.ProductDecriptionInfo.setText(String.valueOf(this.product.getDescripcion()));
        this.ProductImageInfo.setImageBitmap(this.product.getImg());
        this.numberPicker.setMaxValue(10);
        this.numberPicker.setMinValue(1);

        this.initRecyclerViewComentarios(v);

        this.mListener.getFirestoreRealTimeInstance().getComments(product,comments,commentAdapter);

        this.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                product.setCantidad(newVal);
                ProductPriceInfo.setText(String.valueOf(product.getPrecio()*newVal+"$"));
            }
        });

        this.newComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u=mListener.getUserInformation();
                createDialogToCreateComment(u,product);
            }
        });


        return v;
    }

    public void initRecyclerViewComentarios(View v) {
        final View view = v;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewProductsShoppingCart = (RecyclerView) v.findViewById(R.id.RecyclerViewComentarios);
        recyclerViewProductsShoppingCart.setLayoutManager(linearLayoutManager);
        recyclerViewProductsShoppingCart.scheduleLayoutAnimation();
        commentAdapter=new MyCommentsRecyclerViewAdapter(this,comments);
        recyclerViewProductsShoppingCart.setAdapter(commentAdapter);
        recyclerViewProductsShoppingCart.setItemAnimator(new DefaultItemAnimator());

    }

    public void openDialogComment(){
        Log.d("cositas","dialogoABierto");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.comments_dialog, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView commentsRv = (RecyclerView) v.findViewById(R.id.rvComments);
        commentsRv.setLayoutManager(linearLayoutManager);
        commentAdapter=new MyCommentsRecyclerViewAdapter(this,comments);
        commentsRv.setAdapter(commentAdapter);
        commentsRv.setItemAnimator(new DefaultItemAnimator());

        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createDialogToCreateComment(User u, products p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        final String email = u.getEmail();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.comment_dialog_layout, null);

        TextView CommentEmail=(TextView)v.findViewById(R.id.dialogEmailUser);
        CommentEmail.setText(u.getEmail());

        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        EditText comment = (EditText) v.findViewById(R.id.contenidoComentarioDialogo);
                        mListener.getFirestoreRealTimeInstance().insertComment(new CommentsRealTime(comment.getText().toString(),u.getEmail(),p.getImgName()));
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity=(Activity) context;
            mListener=(OnGetUserActions) activity;

        }
    }
}