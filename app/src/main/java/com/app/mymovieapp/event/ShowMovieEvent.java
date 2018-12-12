package com.app.mymovieapp.event;

import com.app.mymovieapp.Objects.Movie;

public class ShowMovieEvent {
    public final Movie movie;

    public ShowMovieEvent(Movie movie) {

        this.movie = movie;
    }
}
