package com.app.mymovieapp.Screens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mymovieapp.R;
import com.app.mymovieapp.event.callback.ReviewsCallback;
import com.app.mymovieapp.event.ShowMovieEvent;
import com.app.mymovieapp.event.UpdateFavoritesEvent;
import com.app.mymovieapp.Objects.Movie;
import com.app.mymovieapp.Objects.Review;
import com.app.mymovieapp.Screens.adapter.ReviewsAdapter;
import com.app.mymovieapp.util.MoviesUtil;
import com.app.mymovieapp.util.Util;
import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import icepick.State;

public class MovieFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    @State
    Movie movie;

    ImageView backdropView;
    TextView titleView;
    TextView releaseDateView;
    TextView ratingView;
    TextView overviewView;
    RecyclerView reviewsView;
    FloatingActionButton favoriteView;


    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        backdropView = rootView.findViewById(R.id.backdrop);
        titleView = rootView.findViewById(R.id.title);
        releaseDateView = rootView.findViewById(R.id.release_date);
        ratingView = rootView.findViewById(R.id.rating);
        overviewView = rootView.findViewById(R.id.overview);
        reviewsView = rootView.findViewById(R.id.reviews);
        favoriteView = rootView.findViewById(R.id.favorite);

        rootView.findViewById(R.id.trailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.openLinkInExternalApp(getContext(), movie.getTrailerUrl());
            }
        });


        rootView.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = MoviesUtil.toggleFavorite(getContext(), movie);
                updateFavoriteFab(isFavorite);
                EventBus.getDefault().postSticky(new UpdateFavoritesEvent());
            }
        });
        if (movie != null) {
            init();
        }


        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
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

    @Subscribe(sticky = true)
    public void onEvent(ShowMovieEvent event) {
        movie = event.movie;
        init();
    }


    @Override
    protected void init() {
        Glide.with(getContext())
                .load(movie.getBackdropUrl())
                .into(backdropView);
        titleView.setText(movie.getTitle());
        releaseDateView.setText(Util.toPrettyDate(movie.getReleaseDate()));
        ratingView.setText(movie.getRating() + "/10");
        overviewView.setText(movie.getOverview());
        reviewsView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsView.setHasFixedSize(false);
        updateFavoriteFab(MoviesUtil.isFavorite(getContext(), movie));
        loadReviews();
    }

    private void updateFavoriteFab(boolean isFavorite) {
        GoogleMaterial.Icon favoriteIcon = isFavorite ?
                GoogleMaterial.Icon.gmd_favorite : GoogleMaterial.Icon.gmd_favorite_border;
        favoriteView.setImageDrawable(new IconicsDrawable(getContext())
                .icon(favoriteIcon)
                .color(Color.WHITE)
                .sizeDp(48));
    }

    public void shareMovie() {
        String text = String.format("%s\n%s", movie.getTitle(), movie.getTrailerUrl());
        Util.shareText(getActivity(), text);
    }

    private void loadReviews() {
        MoviesUtil.getReviewsFromApi(getActivity(), movie, new ReviewsCallback() {
            @Override
            public void success(List<Review> reviews) {
                if (reviewsView != null) {
                    reviewsView.setAdapter(new ReviewsAdapter(getContext(), reviews));
                }
            }

            @Override
            public void error(Exception error) {
                error.printStackTrace();
            }
        });
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
//        ViewHelper.setTranslationY(mImageView, scrollY / 2);
        ((MovieActivity) getActivity()).domagic(alpha, baseColor);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }
}
