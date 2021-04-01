package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Administrator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseMessagingModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationToUsers extends Fragment {

    private EditText tituloNoti;
    private EditText contenidoNoti;
    private RecyclerView recyclerUsuarios;
    private MyUsersAdminRecyclerViewAdapter adapter;
    private ArrayList<User> usuarios;
    private ArrayList<User> usuariosSeleccionados;

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
        this.usuariosSeleccionados=new ArrayList<>();
        usuarios=new ArrayList<>();

        tituloNoti = v.findViewById(R.id.tituloNotificacion);
        contenidoNoti = v.findViewById(R.id.contenidoNotificacion);
        recyclerUsuarios=v.findViewById(R.id.recyclerUsersAdmin);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerUsuarios.setLayoutManager(linearLayoutManager);
        adapter=new MyUsersAdminRecyclerViewAdapter(usuarios);
        recyclerUsuarios.setAdapter(adapter);

        FirebaseCloudFirestore bd = new FirebaseCloudFirestore(getContext());

        bd.getAllUsers(this.usuariosSeleccionados,usuarios,adapter);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), buildNotificationPayload().toString(),
                        Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), "Noasdsadadsasdl",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return v;
    }

    public ArrayList<String> getTokensUsuariosSeleccionados(){
        ArrayList<String> tokens=new ArrayList<>();
        for (User u: this.usuariosSeleccionados) {
            tokens.add(u.getToken());
        }
        return tokens;
    }

    private JsonObject buildNotificationPayload() {
        // compose notification json payload

        JsonObject payload = new JsonObject();

        // String[] destinos={"eKjdpagdTOW-a_2aNtuXCm:APA91bHQMYyrKzkxh63RgDkFc6KTtaG0YgXwNAqgugQWJdKPJTBhh3Zq2EoXGYIBQ5o4J_GI_nSvwW__oHl8ZGEP_CWcKiJTn1UA6uZZKiVDwou-VlVOT2GRTQ0QmcwtmGUy-48NqWUE","dxe1OgeMQe2JAx6amBQc70:APA91bFHhFDt3utKMTb5Ve5DWgmcZTMZ_ocTpOQ_u5ljM20QEMFYNKsIBN0hnIWUtN6VPWynknUxJKsQ8myOXkmbRMDRUGj1XM3ZL7SVbRHUAIsnldMejO_OCurW0qJR-gaiB9W89GDZ"};

        payload.add("registration_ids", new Gson().toJsonTree(getTokensUsuariosSeleccionados()));

        JsonObject data = new JsonObject();
        data.addProperty("title", tituloNoti.getText().toString());
        data.addProperty("body", contenidoNoti.getText().toString());
        // add data payload
        payload.add("notification", data);

        return payload;
    }

}