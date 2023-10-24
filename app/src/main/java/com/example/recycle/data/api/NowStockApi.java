package com.example.recycle.data.api;

import com.example.recycle.data.model.DailyStockModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NowStockApi {
    @GET("getStockInfo.jsp")
    Call<DailyStockModel> CallNowStock(
    @Query("ex_ch") String Code
    );
}
