package com.example.dogapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dogapp.R;
import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    private DogBreed dogBreed;
    private ImageView imgDog;
    private TextView tvName, tvBreedFor, tvBreedGroup, tvLifeSpan, tvOrigin, tvTemperament, tvHeight, tvWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        imgDog = view.findViewById(R.id.img_dog);
        tvName = view.findViewById(R.id.tv_name);
        tvBreedFor = view.findViewById(R.id.tv_breed_for);
        tvBreedGroup = view.findViewById(R.id.tv_breed_group);
        tvLifeSpan = view.findViewById(R.id.tv_life_span);
        tvOrigin = view.findViewById(R.id.tv_origin);
        tvTemperament = view.findViewById(R.id.tv_temperament);
        tvHeight = view.findViewById(R.id.tv_height);
        tvWeight = view.findViewById(R.id.tv_weight);

        Picasso.with(getContext())
                .load(getArguments().getString("URL"))
                .error(R.drawable.ic_baseline_image_24)
                .into(imgDog);
        tvName.setText(getArguments().getString("NAME"));
        tvBreedFor.setText(getArguments().getString("BREED_FOR"));
        tvBreedGroup.setText(getArguments().getString("BREED_GROUP"));
        tvLifeSpan.setText(getArguments().getString("LIFE_SPAN"));
        tvOrigin.setText(getArguments().getString("ORIGIN"));
        tvTemperament.setText(getArguments().getString("TEMPERAMENT"));
        tvHeight.setText(getArguments().getString("HEIGHT") + " cm");
        tvWeight.setText(getArguments().getString("WEIGHT") + " kg");
    }
}