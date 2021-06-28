package com.example.dogapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dogapp.R;

public class ListFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvDogList;
    private DogBreedAdapter dogBreedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        dogBreedAdapter = new DogBreedAdapter(getContext());

        View view = getView();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        rvDogList = view.findViewById(R.id.rv_dog_list);
        rvDogList.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvDogList.setAdapter(dogBreedAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dogBreedAdapter = new DogBreedAdapter(getContext());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ItemTouchHelper.SimpleCallback touchHelperCallbackLeft = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (dogBreedAdapter.getDogBreed(viewHolder.getAdapterPosition()).isShowDetails == false) {
                    dogBreedAdapter.showDetails(viewHolder.getAdapterPosition());
                } else {
                    dogBreedAdapter.hideDetails(viewHolder.getAdapterPosition());
                }

            }
        };

        ItemTouchHelper.SimpleCallback touchHelperCallbackRight = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (dogBreedAdapter.getDogBreed(viewHolder.getAdapterPosition()).isShowDetails == false) {
                    dogBreedAdapter.showDetails(viewHolder.getAdapterPosition());
                } else {
                    dogBreedAdapter.hideDetails(viewHolder.getAdapterPosition());
                }

            }
        };

        ItemTouchHelper itemTouchHelperLeft = new ItemTouchHelper(touchHelperCallbackLeft);
        itemTouchHelperLeft.attachToRecyclerView(rvDogList);

        ItemTouchHelper itemTouchHelperRight = new ItemTouchHelper(touchHelperCallbackRight);
        itemTouchHelperRight.attachToRecyclerView(rvDogList);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dogBreedAdapter.filterList(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}