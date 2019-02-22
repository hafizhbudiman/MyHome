package com.kecepret.myhome.network;

import com.kecepret.myhome.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/tokens")
    Call<TokenResponse> doGetTokens();

}