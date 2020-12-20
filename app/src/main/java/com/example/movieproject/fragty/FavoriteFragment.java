package com.example.movieproject.fragty;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieproject.R;
import com.example.movieproject.adaptors.FavoriteAdapter;
import com.example.movieproject.helper.FavoriteHelper;
import com.example.movieproject.models.FavoriteModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class FavoriteFragment extends Fragment {

    RecyclerView rvFavorite;
    private LinkedList<FavoriteModel> list;
    private FavoriteHelper favoriteHelper;
    private FavoriteAdapter favoriteAdapter;


    public FavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);
        rvFavorite = (RecyclerView) view.findViewById(R.id.rv_favorite);
        return view;
    }

    private class LoadDB extends AsyncTask<Void, Void, ArrayList<FavoriteModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (list.size() > 0 ){
                list.clear();
            }

        }

        @Override
        protected void onPostExecute(ArrayList<FavoriteModel> favorites) {
            super.onPostExecute(favorites);
            list.addAll(favorites);
            favoriteAdapter.setListFavorite(list);
            favoriteAdapter.notifyDataSetChanged();

            if (list.size() == 0){
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<FavoriteModel> doInBackground(Void... voids) {
            return favoriteHelper.query();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (favoriteHelper != null){
            favoriteHelper.close();
        }
    }

    @Override
    public void onResume() {

        rvFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavorite.setHasFixedSize(true);

        favoriteHelper = new FavoriteHelper(getActivity());
        favoriteHelper.open();

        list = new LinkedList<>();

        favoriteAdapter = new FavoriteAdapter(getActivity());
        favoriteAdapter.setListFavorite(list);
        rvFavorite.setAdapter(favoriteAdapter);
        new LoadDB().execute();
        super.onResume();
    }


}
