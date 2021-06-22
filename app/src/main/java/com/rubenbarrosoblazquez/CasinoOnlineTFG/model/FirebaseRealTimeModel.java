package com.rubenbarrosoblazquez.CasinoOnlineTFG.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Comments;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.CommentsRealTime;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.InfoServicios.MyCommentsRecyclerViewAdapter;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.MyProductsRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FirebaseRealTimeModel {

    private Context context;
    private DatabaseReference db;
    private FirebaseStorage storage;
    private FirebaseCloudFirestore cloudFirestore;
    public FirebaseRealTimeModel(Context context) {
        this.context = context;
        this.db = FirebaseDatabase.getInstance().getReference();
        this.storage=FirebaseStorage.getInstance();
        this.cloudFirestore = new FirebaseCloudFirestore(context);

    }

    public void getComments(products p,ArrayList<Comments> comments, MyCommentsRecyclerViewAdapter adapter){
        final int[] i = {0};

        db.child("comments").equalTo(p.getImgName(),"product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        CommentsRealTime comment = new CommentsRealTime(ds.child("comment").getValue().toString(),ds.child("email").getValue().toString(),ds.child("product").getValue().toString());
                        getEmailImage(comment, comments, adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void getEmailImage(CommentsRealTime data,ArrayList<Comments> comments, MyCommentsRecyclerViewAdapter adapter){
        Log.d("cositas",data.toString());
        StorageReference storageRef = storage.getReference();
        StorageReference productRef = storageRef.child("/ProfilesImages/"+data.getEmail().replace(".","_")+".png");
        Log.d("path",storageRef.getBucket()+"-->"+productRef.getPath());

        try{

            productRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .asBitmap()
                            .load(uri)
                            .placeholder(R.drawable.ruleta)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    comments.add(new Comments(data.getComment(),resource,data.getEmail()));
                                    adapter.notifyDataSetChanged();
                                    Log.d("cositas",comments.size()+"");
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                                    comments.add(new Comments(data.getComment(),bf,data.getEmail()));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                            comments.add(new Comments(data.getComment(),bf,data.getEmail()));
                            adapter.notifyDataSetChanged();
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
            Log.d("path", ""+e.getMessage());
        }

    }


    public void insertComment(CommentsRealTime comment){
        final int[] i = {0};
        db.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(i[0] == 0){
                    db.child("comments").child("comment"+(snapshot.getChildrenCount()+1)).setValue(comment);
                }
                i[0]++;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}
