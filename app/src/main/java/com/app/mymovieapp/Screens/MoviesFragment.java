package com.app.mymovieapp.Screens;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.mymovieapp.MyApplication;
import com.app.mymovieapp.R;
import com.app.mymovieapp.event.callback.MoviesCallback;
import com.app.mymovieapp.event.ShowMovieEvent;
import com.app.mymovieapp.event.TwoPaneEvent;
import com.app.mymovieapp.event.UpdateFavoritesEvent;
import com.app.mymovieapp.Objects.Movie;
import com.app.mymovieapp.Screens.adapter.MoviesAdapter;
import com.app.mymovieapp.util.MoviesUtil;
import com.app.mymovieapp.util.Util;
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.State;

public class MoviesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerItemClickSupport.OnItemClickListener {
    private static final String ARG_FRAG_TYPE = "fragType";
    private static final String ARG_FRAG_TWO_PANE = "twoPane";

    public enum Type {
        POPULAR,
        TOP_RATED,
        FAVORITES
    }

    @State
    ArrayList<Movie> movies;
    @State
    Type fragType;
    @State
    boolean twoPane;
    SwipeRefreshLayout refreshView;
    RecyclerView moviesView;
    Dialog dialog;

    MyApplication myApplication;

    public static MoviesFragment newInstance(Type fragType, boolean twoPane) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAG_TYPE, fragType);
        args.putBoolean(ARG_FRAG_TWO_PANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (getArguments() != null) {
            fragType = (Type) getArguments().getSerializable(ARG_FRAG_TYPE);
            twoPane = getArguments().getBoolean(ARG_FRAG_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        dialog = Util.LoadingSpinner(getActivity());

        refreshView = rootView.findViewById(R.id.refresh);
        moviesView = rootView.findViewById(R.id.movies);

        init();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onRefresh() {
        movies = null;
        updateMovies();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        showMovieAtPosition(position);
    }

    @Subscribe(sticky = true)
    public void onEvent(UpdateFavoritesEvent event) {
        if (fragType == Type.FAVORITES) {
            EventBus.getDefault().removeStickyEvent(UpdateFavoritesEvent.class);
            onRefresh();
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(TwoPaneEvent event) {
        twoPane = event.twoPane;
    }

    @Override
    protected void init() {
        RecyclerItemClickSupport.addTo(moviesView)
                .setOnItemClickListener(this);
        moviesView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        moviesView.setHasFixedSize(true);

        dialog.show();
        updateMovies();
    }

    private void updateMovies() {
        if (movies == null) {
            MoviesCallback callback = new MoviesCallback() {
                @Override
                public void success(List<Movie> result) {

                    if (myApplication.getSearch().equals("")) {
                        movies = new ArrayList<>(result);
                    } else {
                        movies = new ArrayList<>();
                        for (Movie movie : result) {
                            if (movie.getTitle().toLowerCase().contains(myApplication.getSearch().toLowerCase())) {
                                movies.add(movie);
                            }
                        }
                    }
                    if (moviesView != null) {
                        moviesView.setAdapter(new MoviesAdapter(getContext(), movies));
                    }
                    refreshView.setRefreshing(false);
                    dialog.dismiss();
                }

                @Override
                public void error(Exception error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    refreshView.setRefreshing(false);
                }
            };
            switch (fragType) {
                case POPULAR:
                    MoviesUtil.getPopularMovies(getActivity(), callback);
                    break;
                case TOP_RATED:
                    MoviesUtil.getTopRatedMovies(getActivity(), callback);
                    break;
                case FAVORITES:
                    MoviesUtil.getFavoritesMovies(getActivity(), callback);
                    break;
            }
        } else if (moviesView != null) {
            moviesView.setAdapter(new MoviesAdapter(getContext(), movies));
            refreshView.setRefreshing(false);
        }
    }

    private void showMovieAtPosition(int position) {
        if (movies != null && position <= movies.size() - 1) {
            Movie movie = movies.get(position);
            EventBus.getDefault().postSticky(new ShowMovieEvent(movie));
            if (!twoPane) {
                startActivity(new Intent(getContext(), MovieActivity.class));

            }
        }
    }
}
