package com.example.dogapp.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.R;
import com.example.dogapp.model.DogAPI;
import com.example.dogapp.model.DogBreed;
import com.example.dogapp.model.DogBreedDao;
import com.example.dogapp.model.DogBreedDatabase;
import com.example.dogapp.viewModel.DogAPIService;
import com.example.dogapp.viewModel.ItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DogBreedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DogBreed> dogBreedList, fullData;

    private DogBreedDao dogBreedDao;

    private final int SHOW_DETAILS = 1;
    private final int HIDE_DETAILS = 2;

    public DogBreedAdapter(Context context) {
        this.context = context;
        dogBreedList = new ArrayList<>();
        fullData = new ArrayList<>();

        DogBreedDatabase dogBreedDatabase = DogBreedDatabase.getInstance(context);
        dogBreedDao = dogBreedDatabase.dogBreedDao();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            Toast.makeText(context, "Internet Available", Toast.LENGTH_SHORT).show();

            DogAPI dogAPI = DogAPIService.getInstance();
            dogAPI.getAllDogs()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                        @Override
                        public void onSuccess(@NonNull List<DogBreed> dogBreedList) {
                            fullData = dogBreedList;
                            setDogBreedList(fullData);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();

            DogBreedDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    fullData = dogBreedDao.getAllDogBreeds();
                    setDogBreedList(fullData);
                }
            });
        }
    }

    public void filterList(String searchStr) {
        List<DogBreed> filteredList = new ArrayList<>();
        for (DogBreed dogBreed : fullData) {
            if (dogBreed.name.contains(searchStr)) {
                filteredList.add(dogBreed);
            }
        }

        setDogBreedList(filteredList);
    }

    public void setDogBreedList(List<DogBreed> dogBreedList) {
        this.dogBreedList = dogBreedList;
        notifyDataSetChanged();
    }

    public DogBreed getDogBreed(int position) {
        return dogBreedList.get(position);
    }

    public void showDetails(int position) {
        if (dogBreedList.get(position).isShowDetails == false) {
            dogBreedList.get(position).isShowDetails = true;
            notifyDataSetChanged();
        }
    }

    public void hideDetails(int position) {
        if (dogBreedList.get(position).isShowDetails == true) {
            dogBreedList.get(position).isShowDetails = false;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dogBreedList.get(position).isShowDetails) {
            return SHOW_DETAILS;
        } else {
            return HIDE_DETAILS;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SHOW_DETAILS) {
            View dogBreedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dog_details_item, parent, false);
            return new DetailsViewHolder(dogBreedView);
        } else {
            View dogBreedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dog_main_item, parent, false);
            return new MainViewHolder(dogBreedView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DogBreedDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dogBreedDao.insertDogBreed(dogBreedList.get(position));
            }
        });

        if (holder instanceof MainViewHolder) {
            ((MainViewHolder) holder).tvName.setText(dogBreedList.get(position).name);
            ((MainViewHolder) holder).tvDes.setText(dogBreedList.get(position).breedFor);

            Picasso.with(context)
                    .load(dogBreedList.get(position).imageUrl)
                    .error(R.drawable.ic_baseline_image_24)
                    .into(((MainViewHolder) holder).imgDog, new Callback() {
                        @Override
                        public void onSuccess() {
                            ((MainViewHolder) holder).prbLoadImg.setVisibility(View.GONE);
                            ((MainViewHolder) holder).imgDog.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            ((MainViewHolder) holder).prbLoadImg.setVisibility(View.GONE);
                            ((MainViewHolder) holder).imgDog.setVisibility(View.VISIBLE);
                        }
                    });

            ((MainViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", dogBreedList.get(position).imageUrl);
                    bundle.putString("NAME", dogBreedList.get(position).name);
                    bundle.putString("BREED_FOR", dogBreedList.get(position).breedFor);
                    bundle.putString("BREED_GROUP", dogBreedList.get(position).breedGroup);
                    bundle.putString("LIFE_SPAN", dogBreedList.get(position).lifeSpan);
                    bundle.putString("ORIGIN", dogBreedList.get(position).origin);
                    bundle.putString("TEMPERAMENT", dogBreedList.get(position).temperament);
                    bundle.putString("HEIGHT", dogBreedList.get(position).height.height);
                    bundle.putString("WEIGHT", dogBreedList.get(position).weight.weight);
                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
                }
            });
        }

        if (holder instanceof DetailsViewHolder) {
            ((DetailsViewHolder) holder).tvBreedFor.setText(dogBreedList.get(position).breedFor);
            ((DetailsViewHolder) holder).tvBreedGroup.setText(dogBreedList.get(position).breedGroup);
            ((DetailsViewHolder) holder).tvLifeSpan.setText(dogBreedList.get(position).lifeSpan);
            ((DetailsViewHolder) holder).tvOrigin.setText(dogBreedList.get(position).origin);
            ((DetailsViewHolder) holder).tvTemperament.setText(dogBreedList.get(position).temperament);
            ((DetailsViewHolder) holder).tvHeight.setText(dogBreedList.get(position).height.height + " cm");
            ((DetailsViewHolder) holder).tvWeight.setText(dogBreedList.get(position).weight.weight + " kg");

            ((DetailsViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", dogBreedList.get(position).imageUrl);
                    bundle.putString("NAME", dogBreedList.get(position).name);
                    bundle.putString("BREED_FOR", dogBreedList.get(position).breedFor);
                    bundle.putString("BREED_GROUP", dogBreedList.get(position).breedGroup);
                    bundle.putString("LIFE_SPAN", dogBreedList.get(position).lifeSpan);
                    bundle.putString("ORIGIN", dogBreedList.get(position).origin);
                    bundle.putString("TEMPERAMENT", dogBreedList.get(position).temperament);
                    bundle.putString("HEIGHT", dogBreedList.get(position).height.height);
                    bundle.putString("WEIGHT", dogBreedList.get(position).weight.weight);
                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dogBreedList.size();
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar prbLoadImg;
        private ImageView imgDog;
        private TextView tvName, tvDes;
        private ItemClickListener itemClickListener;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            prbLoadImg = itemView.findViewById(R.id.prb_load_image);
            imgDog = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_dog_name);
            tvDes = itemView.findViewById(R.id.tv_details);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvBreedFor, tvBreedGroup, tvLifeSpan, tvOrigin, tvTemperament, tvHeight, tvWeight;
        private ItemClickListener itemClickListener;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBreedFor = itemView.findViewById(R.id.details_breed_for);
            tvBreedGroup = itemView.findViewById(R.id.details_breed_group);
            tvLifeSpan = itemView.findViewById(R.id.details_life_span);
            tvOrigin = itemView.findViewById(R.id.details_origin);
            tvTemperament = itemView.findViewById(R.id.details_temperament);
            tvHeight = itemView.findViewById(R.id.details_height);
            tvWeight = itemView.findViewById(R.id.details_weight);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }

}
