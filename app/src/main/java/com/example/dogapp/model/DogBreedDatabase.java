package com.example.dogapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DogBreed.class}, version = 1)
public abstract class DogBreedDatabase extends RoomDatabase {

    public abstract DogBreedDao dogBreedDao();

    private static volatile DogBreedDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static DogBreedDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DogBreedDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DogBreedDatabase.class, "dog_breed_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
