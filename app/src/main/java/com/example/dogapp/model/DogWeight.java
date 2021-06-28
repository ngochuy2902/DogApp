package com.example.dogapp.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class DogWeight {

    @SerializedName("metric")
    @ColumnInfo(name = "Weight")
    public String weight;

//    public DogWeight(String metric) {
//        this.weight = metric;
//    }

}
