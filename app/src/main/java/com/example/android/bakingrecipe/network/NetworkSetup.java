package com.example.android.bakingrecipe.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSetup {
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();
    //private String baseUrl = "http://bazaar-dev.eu-central-1.elasticbeanstalk.com/"''
    private static String baseUrl = "http://go.udacity.com";
    public static Retrofit setupNetwork = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
