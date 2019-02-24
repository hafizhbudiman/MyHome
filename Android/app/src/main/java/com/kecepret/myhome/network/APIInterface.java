package com.kecepret.myhome.network;

import com.kecepret.myhome.model.Door;
import com.kecepret.myhome.model.GoogleId;
import com.kecepret.myhome.model.Lamp;
import com.kecepret.myhome.model.Register;
import com.kecepret.myhome.model.ResponseBE;
import com.kecepret.myhome.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/lamps/turn_on_off/")
    Call<ResponseBE> turnOnOff(@Body Lamp lamp);

    @POST("/doors/lock_unlock/")
    Call<ResponseBE> turnOnOff(@Body Door door);

    @POST("/login/")
    Call<ResponseBE> login(@Body User user);

    @POST("/login/google/")
    Call<ResponseBE> login_google(@Body GoogleId googleId);

    @POST("/users/")
    Call<ResponseBE> register(@Body Register register);
}