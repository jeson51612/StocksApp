package com.example.recycle.data.response;


import com.example.recycle.data.model.DailyStockModel;
import com.example.recycle.data.model.StockModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This class is for multi stock request
public class StockResponse {

    @SerializedName("msgArray")
    @Expose
    private DailyStockModel NowStocks;

}

