package com.rubenbarrosoblazquez.CasinoOnlineTFG.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.*;
import com.google.firebase.messaging.RemoteMessage;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Services.NotificationApiService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseMessagingModel {
    FirebaseMessaging messaging;
    private Context context;
    FirebaseCloudFirestore firestore;

    public FirebaseMessagingModel(Context context) {
        messaging = FirebaseMessaging.getInstance();
        this.context=context;
        firestore=new FirebaseCloudFirestore(context);
    }

    public void recuperarToken(String email){
            // 1
        messaging.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        }else{
                            String token = task.getResult();
                            firestore.setUserToken(email,token);
                            Log.d("TAG",token+"");

                        }

                    }
                });
    }

    public boolean checkGooglePlayServices() {
        // 1
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        // 2
        if (status != ConnectionResult.SUCCESS) {
            Log.e("TAG", "Error");
            return false;
        } else {
            // 3
            Log.i("TAG", "Google play services updated");
            return true;
        }
    }


    public static NotificationApiService getApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(provideClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NotificationApiService.class);
    }

    private static OkHttpClient provideClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(chain -> {
            Request request = chain.request();
            return chain.proceed(request);
        }).build();
    }
}
