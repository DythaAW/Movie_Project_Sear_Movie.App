package com.example.movieproject.fragty;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieproject.R;
import com.example.movieproject.activity.DetailActivity;
import com.example.movieproject.adaptors.MoviesAdapter;
import com.example.movieproject.api.ApiBuilder;
import com.example.movieproject.api.ApiService;
import com.example.movieproject.models.MovieModel;
import com.example.movieproject.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView editsearch;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        editsearch = (SearchView) view.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Inflate the layout for this fragment
        setRetainInstance(true);

    }

    private void loadMovie() {
        String input_movie = editsearch.getQuery().toString();

        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);

        final RecyclerView recyclerView = getActivity().findViewById(R.id.movie_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Call<MovieResponse> call = apiService.getItemSearch(input_movie);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                final List<MovieModel> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getContext()));
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
                            Intent i = new Intent(getContext(), DetailActivity.class);
                            i.putExtra(DetailActivity.EXTRA_TITLE, movies.get(position).getTitle());
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies.get(position).getOverview());
                            i.putExtra(DetailActivity.EXTRA_TIME, movies.get(position).getReleaseDate());
                            i.putExtra(DetailActivity.EXTRA_POSTER, movies.get(position).getPosterPath());
                            getContext().startActivity(i);
                            }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        loadMovie();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
