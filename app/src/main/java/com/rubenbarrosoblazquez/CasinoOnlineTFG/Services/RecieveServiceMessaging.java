package com.rubenbarrosoblazquez.CasinoOnlineTFG.Services;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

public class RecieveServiceMessaging extends FirebaseMessagingService {
    private LocalBroadcastManager broadcastManager=null;
    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleMessage(remoteMessage);

    }

    private void handleMessage( RemoteMessage remoteMessage) {
        //1
        Handler handler = new Handler(Looper.getMainLooper());
        //2
        handler.post(new Runnable() {
            @Override
            public void run() {

                if(remoteMessage!=null){
                    Intent intent = new Intent("informacion");
                    intent.putExtra("contenido", remoteMessage.getNotification().getBody());
                    intent.putExtra("titulo", remoteMessage.getNotification().getTitle());
                    broadcastManager.sendBroadcast(intent);
                }

            }
        });


    }



}
