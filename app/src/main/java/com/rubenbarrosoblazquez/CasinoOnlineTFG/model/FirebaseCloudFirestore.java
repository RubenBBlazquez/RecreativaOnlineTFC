package com.rubenbarrosoblazquez.CasinoOnlineTFG.model;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator.MyUsersAdminRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCloudFirestore {

    private Context context;
    private FirebaseFirestore mFirebaseFirestore;

    public FirebaseCloudFirestore(Context context) {
        this.context = context;
        this.mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void updateSaldo(String user,Float saldo) {
        this.mFirebaseFirestore.collection("users").document(user).update("Saldo",String.valueOf(saldo));
    }
    public void updateSaldoGastado(String user,Float saldoGastado) {
        this.mFirebaseFirestore.collection("users").document(user).update("SaldoGastado",String.valueOf(saldoGastado));
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
        usuario.put("Saldo", u.getSaldo() + "");
        usuario.put("TipoUser", u.getTipoUser() + "");
        usuario.put("DniVerificado",u.isDniVerified()+"");
        usuario.put("TelefonoVerificado",u.isTelefonoVerified()+"");

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

    public void getAllUsers( ArrayList<User> usuariosSeleccionados,ArrayList<User> usuarios, MyUsersAdminRecyclerViewAdapter adapter){
        mFirebaseFirestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> users=task.getResult().getDocuments();
                    String tokens="";
                    for (DocumentSnapshot d: users) {
                        if(d.getString("token")!=null){
                            User u = new User(d.getString("Name"),d.getString("Email"),d.getString("Last name's"),d.getString("provider"));
                            u.setSaldo(Float.valueOf(d.getString("Saldo")));
                            u.setSaldo_gastado(Float.valueOf(d.getString("SaldoGastado")));
                            u.setToken(d.getString("token"));
                            usuarios.add(u);
                            usuariosSeleccionados.add(u);
                        }
                    }

                    adapter.notifyDataSetChanged();

                }else{
                   Log.d("tokens","-error");
                }
            }
        });
    }
}
