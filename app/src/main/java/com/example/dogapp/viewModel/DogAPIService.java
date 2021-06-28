package com.example.dogapp.viewModel;

import com.example.dogapp.model.DogAPI;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogAPIService {

    private static final String BASE_URL = "https://raw.githubusercontent.com/";
    private static DogAPI instance;

    public static DogAPI getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(DogAPI.class);
        }
        return instance;
    }
}
