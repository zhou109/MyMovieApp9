package com.app.mymovieapp.Screens.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mymovieapp.R;
import com.app.mymovieapp.Objects.Review;
import com.app.mymovieapp.util.Util;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.contentView.setText(review.getContent());
        if (!Util.isEmpty(review.getAuthor())) {
            holder.authorView.setText(context.getString(R.string.review_by, review.getAuthor()));
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorView;
        public TextView contentView;

        public ViewHolder(View v) {
            super(v);
            authorView = v.findViewById(R.id.author);
            contentView = v.findViewById(R.id.content);
        }
    }
}
