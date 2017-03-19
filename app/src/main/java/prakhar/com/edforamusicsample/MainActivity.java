package prakhar.com.edforamusicsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import prakhar.com.edforamusicsample.Database.DatabaseHandler;
import prakhar.com.edforamusicsample.Fragments.SongsListFragment;
import prakhar.com.edforamusicsample.Model.SongDetailsModel;

public class MainActivity extends AppCompatActivity {

    MaterialSearchView mSearchView;
    Toolbar mToolbar;
    SongsListFragment songsListFragment;
    DatabaseHandler db;
    boolean searchstatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                if (query.length() > 0) {
                    searchQueryFromDB(query);
                    searchstatus = true;
                } else
                    Toast.makeText(MainActivity.this, "Empty Search String", Toast.LENGTH_LONG).show();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                if (newText.length() != 0) {
                    if (newText.length() > 0) {
                        searchQueryFromDB(newText);
                        searchstatus = true;
                    } else
                        Toast.makeText(MainActivity.this, "Empty Search String", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        if (savedInstanceState == null)

        {
            songsListFragment = new SongsListFragment();
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if (songsListFragment != null) {
                ft.replace(R.id.frame_layout, songsListFragment);
                ft.commit();
            }
        } else {
            //send a variable to a fragment which can abduct api call
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            songsListFragment = (SongsListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "songListFragment");
            if (songsListFragment != null) {
                ft.replace(R.id.frame_layout, songsListFragment);
                ft.commit();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else if (!mSearchView.isSearchOpen()) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
        if (searchstatus == true) {
            searchstatus = false;
            ArrayList<SongDetailsModel> SongsQuery = db.getAllSongs();
            Log.d("size", String.valueOf(SongsQuery.size()));
            if (SongsQuery.size() > 0) {
                songsListFragment.setmSongList(SongsQuery);
            }
        }
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


    private void searchQueryFromDB(String query) {
     //for song and artist
        ArrayList<SongDetailsModel> SongsQuery = db.getAllResultWithQuery(query);
        Log.d("size", String.valueOf(SongsQuery.size()));
        if (SongsQuery.size() > 0) {
            songsListFragment.setmSongList(SongsQuery);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");

        if (getSupportFragmentManager().getFragments().contains(songsListFragment))
            getSupportFragmentManager().putFragment(outState, "playerListFragment", songsListFragment);

        if (songsListFragment != null)
            outState.putParcelableArrayList("ParceableRecordList", songsListFragment.getmSongList());
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        System.out.println("TAG, onRestoreInstanceState");
        ArrayList<SongDetailsModel> mRepoArrayList = savedState.getParcelableArrayList("ParceableRecordList");
        if (mRepoArrayList != null && songsListFragment != null) {
            songsListFragment.setmSongList(mRepoArrayList);
        }
    }


}
