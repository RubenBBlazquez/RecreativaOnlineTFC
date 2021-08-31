package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseMessagingModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationToUsers extends Fragment implements TextWatcher {

    private EditText tituloNoti;
    private EditText contenidoNoti;
    private EditText buscarUsuarios;
    private RecyclerView recyclerUsuarios;
    private MyUsersAdminRecyclerViewAdapter adapter;
    private ArrayList<User> usuarios;
    private  FirebaseCloudFirestore bd;
    public ArrayList<User> usersChecked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_send_notification_to_users, container, false);

        Button b = v.findViewById(R.id.enviarNotificacion);

        usuarios=new ArrayList<>();
        usersChecked = new ArrayList<>();

        tituloNoti = v.findViewById(R.id.tituloNotificacion);
        contenidoNoti = v.findViewById(R.id.contenidoNotificacion);
        buscarUsuarios=v.findViewById(R.id.filtrarUsuaiosAdminNoti);
        buscarUsuarios.addTextChangedListener(this);
        recyclerUsuarios=v.findViewById(R.id.recyclerUsersAdmin);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerUsuarios.setLayoutManager(linearLayoutManager);
        adapter=new MyUsersAdminRecyclerViewAdapter(this,usuarios);
        recyclerUsuarios.setAdapter(adapter);

        bd = new FirebaseCloudFirestore(getContext());
        bd.getAllUsers(usuarios,adapter);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseMessagingModel.getApiService().sendNotification(buildNotificationPayload()).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Notification send successful",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al Enviar las notificaciones",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return v;
    }

    public ArrayList<String> getTokensUsuariosSeleccionados(){
        ArrayList<String> tokens=new ArrayList<>();

        if (usersChecked.size() > 0){

            for (User u: this.usersChecked) {
                tokens.add(u.getToken());
            }

        }else{

            for (User u: this.usuarios) {
                tokens.add(u.getToken());
            }

        }

        return tokens;
    }

    public void deleteUserUnSelected(String email){
        int position = -1;

        for (int i =0 ; i<this.usersChecked.size();i++) {

            User u = this.usersChecked.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                position = i;
                break;
            }

        }

        if (position != -1)
            this.usersChecked.remove(position);
    }

    private JsonObject buildNotificationPayload() {
        // compose notification json payload

        JsonObject payload = new JsonObject();

        payload.add("registration_ids", new Gson().toJsonTree(getTokensUsuariosSeleccionados()));

        JsonObject data = new JsonObject();
        data.addProperty("body", tituloNoti.getText().toString());
        data.addProperty("title", contenidoNoti.getText().toString());
        // add data payload
        payload.add("notification", data);

        return payload;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try{
            if(charSequence.toString().contains("saldo")){
                usuarios.clear();

                String[] cadena=charSequence.toString().split("=");
                Log.d("admin",""+cadena[0]+" - "+cadena[1]+" - "+cadena[0].contains(">"));

                if(cadena[0].contains(">")){
                    Log.d("admin","greater");
                    this.bd.getUsersBySaldoGreaterEqual(usuarios,adapter,Float.valueOf(cadena[1]));

                }else if(cadena[0].contains("<")){
                    Log.d("admin","less");
                    this.bd.getUsersBySaldoLessEqual(usuarios,adapter,Float.valueOf(cadena[1]));

                } else if (!cadena[0].contains("<") && !cadena[0].contains(">")) {
                    Log.d("admin","equal");
                    this.bd.getUsersByEqual(usuarios,adapter,Float.valueOf(cadena[1]));

                }

                adapter.notifyDataSetChanged();

            }else if(charSequence.toString().contains("gasto")){
                usuarios.clear();

            }else if(charSequence.toString().contains("email")){
                usuarios.clear();

            }else if(charSequence.toString().equals("")){
                usuarios.clear();
                bd.getAllUsers(usuarios,adapter);
            }
        }catch (Exception e){
            Log.d("admin",""+e.getMessage());
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}