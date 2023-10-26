package com.example.recycle.data.model;

import java.util.List;

//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//
//public class DailyStockModel implements Parcelable {
//    //Model Class for our stock
//    private String z;
//    private String y;
//
//    public DailyStockModel(String Z,String Y)
//    {
//        Z=z;
//        Y=y;
//    }
//    public static final Creator<DailyStockModel> CREATOR=new Creator<DailyStockModel>() {
//        @Override
//        public DailyStockModel createFromParcel(Parcel parcel) {
//            return null;
//        }
//
//        @Override
//        public DailyStockModel[] newArray(int i) {
//            return new DailyStockModel[0];
//        }
//    };
//
//    //Getters
//
//    public String getNowPrice(){return z;}
//
//    public String getYesterdayPrice(){return y;}
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(z);
//        parcel.writeString(y);
//
//    }
//}
public class DailyStockModel{
    public List<stockDataItem> getMsgArray() {
        return msgArray;
    }

    public String getReferer() {
        return referer;
    }

    public int getUserDelay() {
        return userDelay;
    }

    public String getRtcode() {
        return rtcode;
    }

    public QueryTime getQueryTime() {
        return queryTime;
    }

    public String getRtmessage() {
        return rtmessage;
    }

    public String getExKey() {
        return exKey;
    }

    public int getCachedAlive() {
        return cachedAlive;
    }

    private List<stockDataItem> msgArray;
    private String referer;
    private int userDelay;
    private String rtcode;
    private QueryTime queryTime;
    private String rtmessage;
    private String exKey;
    private int cachedAlive;

    // 省略 getter 和 setter 方法
}

class QueryTime {
    private String sysDate;
    private int stockInfoItem;
    private int stockInfo;
    private String sessionStr;
    private String sysTime;
    private boolean showChart;
    private String sessionFromTime;
    private String sessionLatestTime;

    // 省略 getter 和 setter 方法
}

