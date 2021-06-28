package com.example.dogapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DogBreed {

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public final int id;

    @SerializedName("name")
    @ColumnInfo(name = "Name")
    public final String name;

    @SerializedName("bred_for")
    @ColumnInfo(name = "Breed For")
    public final String breedFor;

    @SerializedName("breed_group")
    @ColumnInfo(name = "Breed Group")
    public final String breedGroup;

    @SerializedName("life_span")
    @ColumnInfo(name = "Life Span")
    public final String lifeSpan;

    @SerializedName("origin")
    @ColumnInfo(name = "Origin")
    public final String origin;

    @SerializedName("temperament")
    @ColumnInfo(name = "Temperament")
    public final String temperament;

    @SerializedName("height")
    @Embedded public final DogHeight height;

    @SerializedName("weight")
    @Embedded public final DogWeight weight;

    @SerializedName("url")
    @ColumnInfo(name = "Image URL")
    public final String imageUrl;

    public boolean isShowDetails;

    public DogBreed(int id, String name, String breedFor, String breedGroup, String lifeSpan, String origin, String temperament, DogHeight height, DogWeight weight, String imageUrl) {
        this.id = id;
        this.name = name;
        this.breedFor = breedFor;
        this.breedGroup = breedGroup;
        this.lifeSpan = lifeSpan;
        this.origin = origin;
        this.temperament = temperament;
        this.height = height;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.isShowDetails = false;
    }

}
