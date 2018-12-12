package com.app.mymovieapp.Screens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.mymovieapp.R;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class MovieActivity extends BaseActivity {

    private MovieFragment movieFrag;
    private View mToolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        menu.findItem(R.id.share)
                .setIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_share)
                        .color(Color.WHITE)
                        .sizeDp(22));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share:
                if (movieFrag != null) {
                    movieFrag.shareMovie();
                }
                break;
        }
        return true;
    }

    @Override
    protected void init() {
        movieFrag = new MovieFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, movieFrag)
                .commit();
    }


    public void domagic(float alpha, int baseColor) {
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));

    }
}
