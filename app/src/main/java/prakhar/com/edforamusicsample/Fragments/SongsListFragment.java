package prakhar.com.edforamusicsample.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import prakhar.com.edforamusicsample.APIController.APIInterface;
import prakhar.com.edforamusicsample.APIController.RetroClient;
import prakhar.com.edforamusicsample.Adapter.SongListAdapter;
import prakhar.com.edforamusicsample.Database.DatabaseHandler;
import prakhar.com.edforamusicsample.Model.SongDetailsModel;
import prakhar.com.edforamusicsample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lendingkart on 3/18/2017.
 */

public class SongsListFragment extends Fragment {

    ListView mPlayerListView;
    private ProgressDialog mProgressDialog;


    ArrayList<SongDetailsModel> mSongList = null;
    SongListAdapter songListAdapter;
    DatabaseHandler db;

    public void setmSongList(ArrayList<SongDetailsModel> mSongList) {
        this.mSongList = mSongList;
        songListAdapter.addData(this.mSongList);
    }

    public ArrayList<SongDetailsModel> getmSongList() {
        return mSongList;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songslistfragment, container, false);

        db = new DatabaseHandler(getActivity());

        mProgressDialog = displayProgressDialog(getActivity());

        mPlayerListView = (ListView) view.findViewById(R.id.song_listview);

        mPlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSongList.get(position) != null) {
                    Bundle arg = new Bundle();
                    arg.putParcelableArrayList("SongParcelableArrayList", mSongList);
                    arg.putInt("SongCurrentPosition", position);

                    SongPlayerFragment songPlayerFragment = new SongPlayerFragment();
                    songPlayerFragment.setArguments(arg);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    if (songPlayerFragment != null) {
                        ft.replace(R.id.frame_layout, songPlayerFragment);
                        ft.addToBackStack("songPlayerFragment");
                        ft.commit();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Song Record Found", Toast.LENGTH_LONG).show();
                }
            }
        });

        songListAdapter = new SongListAdapter(getActivity(), new ArrayList<SongDetailsModel>());
        mPlayerListView.setAdapter(songListAdapter);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mSongList == null) {
            callSongRecordsAPI();
        }
    }

    private void callSongRecordsAPI() {
        showProgress();
        if (prakhar.com.edforamusicsample.Utils.AppStatus.getInstance(getActivity()).isOnline()) {

            Call<List<SongDetailsModel>> SongRecordCall = RetroClient.getInstance().getRetrofit().create(APIInterface.class).GET_SONG_LIST();
            SongRecordCall.enqueue(new Callback<List<SongDetailsModel>>() {
                @Override
                public void onResponse(Call<List<SongDetailsModel>> call, Response<List<SongDetailsModel>> response) {
                    mSongList = new ArrayList<SongDetailsModel>();
                    mSongList = (ArrayList<SongDetailsModel>) response.body();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edfora Songs");
                    if (mSongList.size() > 0) {
                        songListAdapter.addData(mSongList);
                        if (db.getContactsCount() == 0)
                            insertInToSQLiteDB(mSongList);
                        else if (db.getContactsCount() > 0) {
                            db.deleteContact();
                            insertInToSQLiteDB(mSongList);
                        }
                        dismissProgress();
                    } else {
                        dismissProgress();
                        Toast.makeText(getActivity(), "No Song Record Found", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<List<SongDetailsModel>> call, Throwable t) {
                    dismissProgress();
                    if (!t.getMessage().equals(null))
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("failure", t.getMessage());
                }
            });
        } else {
            dismissProgress();
            NoInternetFragment noInternetFragment = new NoInternetFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            if (noInternetFragment != null) {
                ft.replace(R.id.frame_layout, noInternetFragment);
                ft.commit();
            }
        }
    }


    //TODO
    private void insertInToSQLiteDB(ArrayList<SongDetailsModel> mRecordList) {
        for (int i = 0; i < mRecordList.size(); i++) {
            db.addContact(mRecordList.get(i));
        }
        Toast.makeText(getActivity(), "Saved List into DB Successfully", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getmSongList() != null)
            songListAdapter.addData(getmSongList());
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }


    public static ProgressDialog displayProgressDialog(Context context) {
        ProgressDialog progressDialog = ProgressDialog.show(context, "", "", true);
        progressDialog.setContentView(R.layout.tresbudialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        assert progressDialog.getWindow() != null;
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();
        return progressDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}

