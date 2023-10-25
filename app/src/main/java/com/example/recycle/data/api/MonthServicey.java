package com.example.recycle.data.api;

import com.example.recycle.data.repository.credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonthServicey {
    private static Retrofit.Builder retrofitBuilder=
            new Retrofit.Builder()
                    .baseUrl(credentials.Month_Base_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit=retrofitBuilder.build();

    private static MonthStockApi stockApi=retrofit.create(MonthStockApi.class);

    public static MonthStockApi getNowStockApi(){
        return stockApi;
    }
}
