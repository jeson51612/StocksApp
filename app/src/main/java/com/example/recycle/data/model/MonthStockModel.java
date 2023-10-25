package com.example.recycle.data.model;

import java.util.List;

public class MonthStockModel
{
    public String getStat() {
        return stat;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<List<String>> getData() {
        return data;
    }

    public List<String> getNotes() {
        return notes;
    }

    public String getTotal() {
        return total;
    }

    public String stat;
    public String date;
    public String title;
    public List<String> fields;

    public List<List<String>> data;
    public List<String> notes;

    public String total;

}
