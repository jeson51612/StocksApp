package com.example.recycle.data.model;

import static java.lang.System.in;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StockModel {
    public String Code;
    public String Name;
    public String TradeVolume;
    public String TradeValue;
    public String OpeningPrice;
    public String HighestPrice;
    public String LowestPrice;
    public String ClosingPrice;
    public String Change;

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getTradeVolume() {
        return TradeVolume;
    }

    public String getTradeValue() {
        return TradeValue;
    }

    public String getOpeningPrice() {
        return OpeningPrice;
    }

    public String getHighestPrice() {
        return HighestPrice;
    }

    public String getLowestPrice() {
        return LowestPrice;
    }

    public String getClosingPrice() {
        return ClosingPrice;
    }

    public String getChange() {
        return Change;
    }

    public String getTransaction() {
        return Transaction;
    }

    public String Transaction;

}
