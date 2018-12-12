package com.app.mymovieapp.Screens.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.mymovieapp.R;
import com.app.mymovieapp.Objects.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide.with(context)
                .load(movie.getPosterUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.poster_placeholder))
                .into(holder.posterView);
        holder.Name.setText(movie.getTitle());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterView;
        private TextView Name;

        public ViewHolder(View v) {
            super(v);
            Name = v.findViewById(R.id.Name);
            posterView = v.findViewById(R.id.poster);
        }
    }
}
