package com.app.mymovieapp.event.callback;

import com.app.mymovieapp.Objects.Movie;

import java.util.List;

public interface MoviesCallback {

    void success(List<Movie> movies);

    void error(Exception error);

}
