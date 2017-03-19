package prakhar.com.edforamusicsample.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import prakhar.com.edforamusicsample.Model.SongDetailsModel;
import prakhar.com.edforamusicsample.R;

/**
 * Created by lendingkart on 3/18/2017.
 */

public class SongPlayerFragment extends Fragment implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnBufferingUpdateListener {


    private SeekBar seekBarProgress;
    private ProgressDialog mProgressDialog;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class

    private final Handler handler = new Handler();


    TextView songname, artistname;
    ImageView cover, prev, pause, play, stop, next;
    ArrayList<SongDetailsModel> songlist;
    int currentpos;
    private int playbackPosition = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_player, container, false);

        Bundle args = getArguments();
        songlist = args.getParcelableArrayList("SongParcelableArrayList");
        currentpos = args.getInt("SongCurrentPosition");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);

        mProgressDialog = displayProgressDialog(getActivity());

        seekBarProgress = (SeekBar) view.findViewById(R.id.SeekBarTestPlay);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);

        songname = (TextView) view.findViewById(R.id.song_name);
        artistname = (TextView) view.findViewById(R.id.artist_name);
        cover = (ImageView) view.findViewById(R.id.cover_photo);
        prev = (ImageView) view.findViewById(R.id.prev_song);
        pause = (ImageView) view.findViewById(R.id.pause_song);
        play = (ImageView) view.findViewById(R.id.play_song);
        stop = (ImageView) view.findViewById(R.id.stop_song);
        next = (ImageView) view.findViewById(R.id.next_song);


        songname.setText(songlist.get(currentpos).getSong());
        artistname.setText("Artists : " + songlist.get(currentpos).getArtists());


        Glide.with(getActivity())
                .load(songlist.get(currentpos).getCoverImage())
                .fitCenter()
                .placeholder(R.drawable.circular_bar)
                .crossFade()
                .into(cover);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        prev.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);


        return view;
    }


    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.play_song:
                /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */

                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(playbackPosition);
                    mediaPlayer.start();
                }

                showProgress();

                startplayback(currentpos);

                primarySeekBarProgressUpdater();
                break;

            case R.id.pause_song:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
                break;


            case R.id.stop_song:

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playbackPosition = 0;
                }

                break;


            case R.id.next_song:

                showProgress();

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playbackPosition = 0;
                }

                currentpos++;

                refreshViewsWithPosition(currentpos);

                startplayback(currentpos);

                primarySeekBarProgressUpdater();

                break;


            case R.id.prev_song:

                showProgress();

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playbackPosition = 0;
                }

                currentpos--;
                refreshViewsWithPosition(currentpos);

                startplayback(currentpos);

                primarySeekBarProgressUpdater();
                break;
        }
    }

    public void startplayback(int currentpos) {
        try {
            mediaPlayer.setDataSource(songlist.get(currentpos).getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismissProgress();
        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
    }

    private void refreshViewsWithPosition(int currentpos) {

        songname.setText(songlist.get(currentpos).getSong());
        artistname.setText("Artists : " + songlist.get(currentpos).getArtists());


        Glide.with(getActivity())
                .load(songlist.get(currentpos).getCoverImage())
                .centerCrop()
                .placeholder(R.drawable.circular_bar)
                .crossFade()
                .into(cover);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.SeekBarTestPlay) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
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
        killMediaPlayer();
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

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
