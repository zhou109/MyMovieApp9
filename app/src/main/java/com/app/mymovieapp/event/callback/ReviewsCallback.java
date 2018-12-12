package com.app.mymovieapp.event.callback;

import com.app.mymovieapp.Objects.Review;

import java.util.List;

public interface ReviewsCallback {

    void success(List<Review> reviews);

    void error(Exception error);

}
