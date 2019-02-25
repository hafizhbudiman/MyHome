package com.kecepret.myhome.network;

import com.kecepret.myhome.model.Door;
import com.kecepret.myhome.model.GoogleId;
import com.kecepret.myhome.model.Lamp;
import com.kecepret.myhome.model.NotificationResponse;
import com.kecepret.myhome.model.Register;
import com.kecepret.myhome.model.ResponseBE;
import com.kecepret.myhome.model.Token;
import com.kecepret.myhome.model.TokenResponse;
import com.kecepret.myhome.model.User;
import com.kecepret.myhome.model.Username;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("/lamps/turn_on_off/")
    Call<ResponseBE> turnOnOff(@Body Lamp lamp);

    @POST("/lamps/turn_off_all/")
    Call<ResponseBE> turnOffAll(@Body Username username);

    @POST("/doors/lock_unlock/")
    Call<ResponseBE> lockUnlock(@Body Door door);

    @POST("/login/")
    Call<ResponseBE> login(@Body User user);

    @POST("/login/google/")
    Call<ResponseBE> login_google(@Body GoogleId googleId);

    @POST("/users/")
    Call<ResponseBE> register(@Body Register register);

    @POST("/usertokens/")
    Call<ResponseBE> send_token(@Body Token token);

    @GET("/notifications/")
    Call<NotificationResponse> get_notification(@Query("username") String username);
}