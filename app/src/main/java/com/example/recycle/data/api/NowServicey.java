package com.example.recycle.data.api;

import com.example.recycle.data.repository.credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowServicey {
    private static Retrofit.Builder retrofitBuilder=
            new Retrofit.Builder()
                    .baseUrl(credentials.Today_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit=retrofitBuilder.build();

    private static NowStockApi stockApi=retrofit.create(NowStockApi.class);

    public static NowStockApi getNowStockApi(){
        return stockApi;
    }
}
