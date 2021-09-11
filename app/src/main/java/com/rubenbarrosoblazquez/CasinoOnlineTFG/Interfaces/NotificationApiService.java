package com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces;

import com.google.gson.JsonObject;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {
    @Headers({
            "Authorization: key=AAAAYH4eQcM:APA91bG9kBZtYAO5-kxavkZTFw-rTsFwFiSY83omUYc55E6FCQmD1MUZntKjQ4EFMqefNxRylgwgld5shSD7nJf9MVu1Rh_-9Qwv2SlCkoN7xBn12Ubvm7tjlSOlM3a-VEKjGNLDcS_V",
            "Content-Type:application/json"
    })
    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body JsonObject payload);
}
