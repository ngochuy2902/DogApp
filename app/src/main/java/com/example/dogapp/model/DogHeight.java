package com.example.dogapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class DogHeight {

    @SerializedName("metric")
    @ColumnInfo(name = "Height")
    public String height;

//    public DogHeight(String metric) {
//        this.height = metric;
//    }

}
