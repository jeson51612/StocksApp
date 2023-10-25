package com.example.recycle.data.api;

import com.example.recycle.data.model.DailyStockModel;
import com.example.recycle.data.model.MonthStockModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MonthStockApi {
    @GET("STOCK_DAY?response=json")
    Call<MonthStockModel> CallMonthStock(
            @Query("date") String History_Day,
            @Query("stockNo") String Code
    );
}
