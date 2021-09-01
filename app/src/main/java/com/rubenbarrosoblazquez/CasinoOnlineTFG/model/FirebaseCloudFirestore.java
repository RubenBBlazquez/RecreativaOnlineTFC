package com.rubenbarrosoblazquez.CasinoOnlineTFG.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Compras;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator.MyUsersAdminRecyclerViewAdapter;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile.MyBuysRecyclerViewAdapter;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.MyProductsRecyclerViewAdapter;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.TragaPerras.TragaPerrasFragment;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCloudFirestore {

    private Context context;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseStorage storage;

    public FirebaseCloudFirestore(Context context) {
        this.context = context;
        this.mFirebaseFirestore = FirebaseFirestore.getInstance();
        this.storage=FirebaseStorage.getInstance();
    }

    public void updateSaldo(String user,Float saldo) {
        this.mFirebaseFirestore.collection("users").document(user).update("Saldo",saldo);
    }
    public void updateSaldoGastado(String user,Float saldoGastado) {
        this.mFirebaseFirestore.collection("users").document(user).update("SaldoGastado",saldoGastado);
    }

    public boolean updateUser(User u) {
        HashMap<String, Object> usuario = new HashMap<>();

        usuario.put("Email", u.getEmail());
        usuario.put("Name", u.getName());
        usuario.put("Last name's", u.getApellidos());
        usuario.put("Direction", u.getDirection());
        usuario.put("Phone", u.getPhone());
        usuario.put("Dni", u.getDni());
        usuario.put("Verified", u.getVerified());
        usuario.put("Provider", u.getProvider());
        usuario.put("Saldo", u.getSaldo());
        usuario.put("SaldoGastado", u.getSaldo_gastado());
        usuario.put("TipoUser", u.getTipoUser() + "");
        usuario.put("DniVerificado",u.isDniVerified()+"");
        usuario.put("TelefonoVerificado",u.isTelefonoVerified()+"");
        usuario.put("isSkip",u.isSkip());

        try{
            this.mFirebaseFirestore.collection("users").document(u.getEmail()).update(usuario);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void setUserToken(String email,String token){
        try{
            this.mFirebaseFirestore.collection("users").document(email).update("token",token);
        }catch (Exception e){

        }
    }

    public void getAllUsers( ArrayList<User> usuarios, MyUsersAdminRecyclerViewAdapter adapter){
        mFirebaseFirestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> users=task.getResult().getDocuments();
                    String tokens="";
                    for (DocumentSnapshot d: users) {
                        if(d.getString("token")!=null){
                            User u = new User(d.getString("Name"),d.getString("Email"),d.getString("Last name's"),d.getString("provider"));
                            u.setSaldo(Float.valueOf(String.valueOf( d.get("Saldo"))));
                            u.setSaldo_gastado(Float.valueOf(String.valueOf( d.get("SaldoGastado"))));
                            u.setToken(d.getString("token"));
                            usuarios.add(u);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }else{
                   Log.d("tokens","-error");
                }
            }
        });
    }

    public void getUsersBySaldoGreaterEqual(ArrayList<User> usuarios, MyUsersAdminRecyclerViewAdapter adapter,float saldo){
        mFirebaseFirestore.collection("users").whereGreaterThanOrEqualTo("Saldo",saldo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> users=task.getResult().getDocuments();
                    String tokens="";
                    for (DocumentSnapshot d: users) {
                        if(d.getString("token")!=null){
                            User u = new User(d.getString("Name"),d.getString("Email"),d.getString("Last name's"),d.getString("provider"));
                            u.setSaldo(Float.valueOf(String.valueOf( d.get("Saldo"))));
                            u.setSaldo_gastado(Float.valueOf(String.valueOf( d.get("SaldoGastado"))));
                            u.setToken(d.getString("token"));
                            usuarios.add(u);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }else{
                    Log.d("tokens","-error");
                }
            }
        });
    }

    public void getUsersBySaldoLessEqual(ArrayList<User> usuarios, MyUsersAdminRecyclerViewAdapter adapter,float saldo){
        mFirebaseFirestore.collection("users").whereLessThanOrEqualTo("Saldo",saldo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> users=task.getResult().getDocuments();

                    for (DocumentSnapshot d: users) {
                        if(d.getString("token")!=null){
                            User u = new User(d.getString("Name"),d.getString("Email"),d.getString("Last name's"),d.getString("provider"));
                            u.setSaldo(Float.valueOf(String.valueOf( d.get("Saldo"))));
                            u.setSaldo_gastado(Float.valueOf(String.valueOf( d.get("SaldoGastado"))));
                            u.setToken(d.getString("token"));
                            usuarios.add(u);
                            adapter.notifyDataSetChanged();
                            Log.d("tokens",u.getEmail());

                        }
                    }

                }else{
                    Log.d("tokens","-error");
                }
            }
        });
    }

    public void getUsersByEqual(ArrayList<User> usuarios, MyUsersAdminRecyclerViewAdapter adapter,float saldo){
        mFirebaseFirestore.collection("users").whereEqualTo("Saldo",saldo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> users=task.getResult().getDocuments();
                    String tokens="";
                    for (DocumentSnapshot d: users) {
                        if(d.getString("token")!=null){
                            User u = new User(d.getString("Name"),d.getString("Email"),d.getString("Last name's"),d.getString("provider"));
                            u.setSaldo(Float.valueOf(String.valueOf( d.get("Saldo"))));
                            u.setSaldo_gastado(Float.valueOf(String.valueOf( d.get("SaldoGastado"))));
                            u.setToken(d.getString("token"));
                            usuarios.add(u);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }else{
                    Log.d("tokens","-error");
                }
            }
        });
    }


    public void saveProfilePic(Bitmap pic,User u){
        StorageReference storageRef = storage.getReference();
        StorageReference profilePicRef = storageRef.child("/ProfilesImages/"+(u.getEmail().replace(".","_"))+".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "error al guardar la imagen en la base de datos, comprueba la conexion a intenet", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, taskSnapshot.getMetadata().getName(), Toast.LENGTH_SHORT).show();               // ...
            }
        });

    }

    public void getProfilePicFromStorage(ImageView imagen,String email) {
        StorageReference storageRef = storage.getReference();
        StorageReference profileRef = storageRef.child("/ProfilesImages/"+email.replace(".","_")+".png");
        Log.d("path",storageRef.getBucket()+"-->"+profileRef.getPath());

        try{

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri)
                            .override(100,100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ruleta)
                            .into(imagen);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imagen.setImageResource(R.drawable.ruleta);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Log.d("path", ""+e.getMessage());
        }

    }


    public void getAllProductsByType(ArrayList<products> products, String type, MyProductsRecyclerViewAdapter adapter){
        mFirebaseFirestore.collection("productos").whereEqualTo("type",type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> productos=task.getResult().getDocuments();

                    for (DocumentSnapshot d: productos) {
                        // public products(String descripcion, Bitmap DIR_IMG, String nombre, int n_bastidor, double precio, String tipo, int cantidad) {
                        com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products product = new products(d.getString("imageName"),d.getString("description"),d.getString("name"),Double.valueOf(d.getString("price")),d.getString("type"),Integer.parseInt(d.getString("unidades")));
                        getProductImage(products,d.getString("imageName"),adapter,product);
                    }

                }
            }
        });
    }

    public void getProductsByName(String name,String type, MyProductsRecyclerViewAdapter adapter, ArrayList<products> products){
        products.clear();
        if(name.equalsIgnoreCase("")){
            getAllProductsByType(products,type,adapter);
        }else{
            mFirebaseFirestore.collection("productos").whereEqualTo("type",type).whereEqualTo("name",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        List<DocumentSnapshot> productos=task.getResult().getDocuments();

                        for (DocumentSnapshot d: productos) {
                            // public products(String descripcion, Bitmap DIR_IMG, String nombre, int n_bastidor, double precio, String tipo, int cantidad) {
                            com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.products product = new products(d.getString("imageName"),d.getString("description"),d.getString("name"),Double.valueOf(d.getString("price")),d.getString("type"),Integer.parseInt(d.getString("unidades")));
                            getProductImage(products,d.getString("imageName"),adapter,product);
                        }
                    }
                }
            });
        }

    }

    public void getProductImage(ArrayList<products> products,String identifier,MyProductsRecyclerViewAdapter adapter,products product){
        StorageReference storageRef = storage.getReference();
        StorageReference productRef = storageRef.child("/productImages/"+identifier+".png");
        Log.d("path",storageRef.getBucket()+"-->"+productRef.getPath());

        try{

            productRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .asBitmap()
                            .load(uri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    product.setImg(resource);
                                    products.add(product);
                                    try{
                                        adapter.notifyDataSetChanged();
                                    }catch (Exception er){
                                        Log.d("error",er.getMessage());
                                    }                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                                    product.setImg(bf);
                                    products.add(product);

                                    try{
                                        adapter.notifyDataSetChanged();
                                    }catch (Exception er){
                                        Log.d("error",er.getMessage());
                                    }
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                            product.setImg(bf);
                            products.add(product);
                            try{
                                adapter.notifyDataSetChanged();
                            }catch (Exception er){
                                Log.d("error",er.getMessage());
                            }                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
            Log.d("path", ""+e.getMessage());
        }

    }
    public void getProductImageBuys(ArrayList<Compras> compras,String identifier,MyBuysRecyclerViewAdapter adapter,Compras c){
        StorageReference storageRef = storage.getReference();
        StorageReference productRef = storageRef.child("/productImages/"+identifier+".png");
        Log.d("path",storageRef.getBucket()+"-->"+productRef.getPath());

        try{

            productRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .asBitmap()
                            .load(uri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    c.setImagen(resource);
                                    compras.add(c);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                                    c.setImagen(bf);
                                    compras.add(c);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Bitmap bf = BitmapFactory.decodeResource(context.getResources(),R.drawable.ruleta);
                            c.setImagen(bf);
                            compras.add(c);
                            adapter.notifyDataSetChanged();
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
            Log.d("path", ""+e.getMessage());
        }

    }

    public void saveProductImage(String imageName,Bitmap image){
        StorageReference storageRef = storage.getReference();
        StorageReference profilePicRef = storageRef.child("/productImages/"+imageName+".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, context.getString(R.string.productImageSavedError), Toast.LENGTH_SHORT).show();               // ...
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, context.getString(R.string.productImageSaved)+"("+taskSnapshot.getMetadata().getName()+")", Toast.LENGTH_SHORT).show();               // ...
            }
        });
    }


    public void insertProduct(products p){

        mFirebaseFirestore.collection("productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    long count=task.getResult().getDocuments().stream().count();

                    Map<String,String> data = new HashMap<>();
                    data.put("description",p.getDescripcion());
                    data.put("imageName","product"+(count+1));
                    data.put("name",p.getNombre());
                    data.put("type",p.getTipo());
                    data.put("unidades",p.getCantidad()+"");
                    data.put("price",p.getPrecio()+"");

                    mFirebaseFirestore.collection("productos")
                            .document("product"+(count+1))
                            .set(data);

                    saveProductImage("product"+(count+1),p.getImg());

            }
        }
    });

}
    public void insertBuy(products p , User u) {
        Log.d("cositas","casdadasd");
        mFirebaseFirestore.collection("compras").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    long count = task.getResult().getDocuments().stream().count();

                    Map<String, Object> data = new HashMap<>();
                    data.put("email", u.getEmail());
                    data.put("productId", p.getImgName());
                    data.put("name", p.getNombre());
                    data.put("type", p.getTipo());
                    data.put("unidades", p.getCantidad());
                    data.put("price", p.getPrecio());
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    data.put("date", date);
                    data.put("isUsed",false);
                    data.put("identificadorCompra","compra" + (count + 1));

                    mFirebaseFirestore.collection("compras")
                            .document("compra" + (count + 1))
                            .set(data);

                    Compras c = new Compras(new Date().toString(), p.getNombre(),String.valueOf(p.getPrecio()),String.valueOf(0), p.getTipo());
                    c.setUsed(false);
                    c.setId("compra" + (count + 1));
                    u.getActiveServicesRuleta().add(c);

                }else{
                    Log.d("cositas","error");
                }

            }

        });
    }

    public void getAllBuysByUser(User u, ArrayList<Compras> comprasAL, MyBuysRecyclerViewAdapter adapter){
        mFirebaseFirestore.collection("compras").whereEqualTo("email",u.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> compras = task.getResult().getDocuments();
                    for (DocumentSnapshot d : compras) {
                        Compras c = new Compras(d.getDate("date").toString(),d.getString("name"),String.valueOf(d.getDouble("price")),String.valueOf(d.getLong("unidades")),d.getString("type"));
                        getProductImageBuys(comprasAL,d.getString("productId"),adapter,c);
                    }
                }
            }
        });
    }

    public void getAllBuysOrderByPrice(User u, ArrayList<Compras> comprasAL, MyBuysRecyclerViewAdapter adapter, Query.Direction direction,String field){
        comprasAL.clear();
        mFirebaseFirestore.collection("compras").whereEqualTo("email",u.getEmail()).orderBy(field, direction)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> compras = task.getResult().getDocuments();
                    for (DocumentSnapshot d : compras) {
                        Compras c = new Compras(d.getDate("date").toString(),d.getString("name"),String.valueOf(d.getDouble("price")),String.valueOf(d.getLong("unidades")),d.getString("type"));
                        getProductImageBuys(comprasAL,d.getString("productId"),adapter,c);
                    }
                }
            }
        });


    }

    public void setMultipliersInSlotMachineGame(TragaPerrasFragment.multiplicatorAdapter adapter, ArrayList<String> multiplicators,String email){

            mFirebaseFirestore.collection("compras")
                    .whereEqualTo("type", "slot_machine")
                    .whereEqualTo("email",email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document:task.getResult().getDocuments()) {
                            if (!isMultiplicatorAlreadyAdded(multiplicators,document.getString("name")))
                                multiplicators.add(document.getString("name"));
                        }
                    }
                    multiplicators.sort(new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.replace("x","").compareTo(o2.replace("x",""));
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            });


    }

    public boolean isMultiplicatorAlreadyAdded(ArrayList<String> multiplicators,String multiplicator){
        for (String multiplier:multiplicators) {
            if (multiplier.equalsIgnoreCase(multiplicator))
                return true;

        }

        return false;
    }

    public void updateUsedStateOfProduct(String id){
        this.mFirebaseFirestore.collection("compras").document(id).update("isUsed",true);
    }


}
