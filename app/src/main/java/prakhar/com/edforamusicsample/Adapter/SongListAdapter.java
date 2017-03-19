package prakhar.com.edforamusicsample.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import prakhar.com.edforamusicsample.CustomView.CustomFontTextView;
import prakhar.com.edforamusicsample.Model.SongDetailsModel;
import prakhar.com.edforamusicsample.R;

import java.util.ArrayList;

/**
 * Created by lendingkart on 3/12/2017.
 */

public class SongListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<SongDetailsModel> mSongList;
    SongViewHolder viewHolder;
    MediaPlayer mediaPlayer;

    public SongListAdapter(Context context, ArrayList<SongDetailsModel> cricketRecordModels) {
        mContext = context;
        mSongList = cricketRecordModels;

        mediaPlayer = new MediaPlayer();
    }

    public void addData(ArrayList<SongDetailsModel> recordList) {
        mSongList = recordList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.song_item_row, parent, false);
            viewHolder = new SongViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SongViewHolder) convertView.getTag();
        }

        viewHolder.SongName.setText(mSongList.get(position).getSong());
        viewHolder.ArtistName.setText(mSongList.get(position).getArtists());

        Glide.with(mContext)
                .load(mSongList.get(position).getCoverImage())
                .centerCrop()
                .placeholder(R.drawable.circular_bar)
                .crossFade()
                .into(viewHolder.Cover);

        viewHolder.PlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.setDataSource(mSongList.get(position).getUrl());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.DownloadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // DownloadTask.execute();
            }
        });

        return convertView;
    }

    private class SongViewHolder {

        CustomFontTextView SongName;
        ImageView PlaySong;
        ImageView Cover;
        ImageView DownloadSong;
        CustomFontTextView ArtistName;

        SongViewHolder(View view) {
            Cover = (ImageView) view.findViewById(R.id.cover_pic);
            SongName = (CustomFontTextView) view.findViewById(R.id.songtitle);
            ArtistName = (CustomFontTextView) view.findViewById(R.id.artist);
            PlaySong = (ImageView) view.findViewById(R.id.play_song);
            DownloadSong = (ImageView) view.findViewById(R.id.download_song);
        }
    }
}
