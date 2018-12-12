package com.app.mymovieapp.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.mymovieapp.MyApplication;
import com.app.mymovieapp.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends BaseActivity {

    MaterialSearchView searchView;
    MoviesFragment.Type Current;

    MyApplication myApplication;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        Title = findViewById(R.id.Tittle);
        myApplication = (MyApplication) getApplicationContext();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                myApplication.setSearch(query);
                getSupportFragmentManager().beginTransaction().add(R.id.container, MoviesFragment.newInstance(Current, false)).commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.container, MoviesFragment.newInstance(MoviesFragment.Type.POPULAR, false)).commit();
        getSupportActionBar().setTitle("");
        ChangeTittle("Popular");

        Current = MoviesFragment.Type.POPULAR;


        init();
    }

    @Override
    protected void init() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem item = menu.findItem(R.id.Search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.Search:

                break;
            case R.id.Sort:
                final String[] fonts = {"Popular", "Top Ratted", "Favourite"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please Chose");
                builder.setItems(fonts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myApplication.setSearch("");
                        if ("Popular".equals(fonts[which])) {
                            Current = MoviesFragment.Type.POPULAR;
                            getSupportFragmentManager().beginTransaction().add(R.id.container, MoviesFragment.newInstance(MoviesFragment.Type.POPULAR, false)).commit();
                        } else if ("Top Ratted".equals(fonts[which])) {
                            Current = MoviesFragment.Type.TOP_RATED;
                            getSupportFragmentManager().beginTransaction().add(R.id.container, MoviesFragment.newInstance(MoviesFragment.Type.TOP_RATED, false)).commit();
                        } else if ("Favourite".equals(fonts[which])) {
                            Current = MoviesFragment.Type.FAVORITES;
                            getSupportFragmentManager().beginTransaction().add(R.id.container, MoviesFragment.newInstance(MoviesFragment.Type.FAVORITES, false)).commit();
                        }
                        ChangeTittle(fonts[which]);

                    }
                });
                builder.show();
                break;
        }
        return true;
    }

    public void ChangeTittle(String Text) {
        Title.setText(Text);
    }

}
