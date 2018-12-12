package com.app.mymovieapp;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class MyApplication extends Application {

    String Search = "";

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }
}
