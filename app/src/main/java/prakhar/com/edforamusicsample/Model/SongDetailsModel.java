package prakhar.com.edforamusicsample.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongDetailsModel implements Parcelable {

    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("artists")
    @Expose
    private String artists;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;

    public SongDetailsModel(Parcel in) {
        if (in != null) {
            song = in.readString();
            url = in.readString();
            artists = in.readString();
            coverImage = in.readString();
        }
    }

    public static final Creator<SongDetailsModel> CREATOR = new Creator<SongDetailsModel>() {
        @Override
        public SongDetailsModel createFromParcel(Parcel in) {
            return new SongDetailsModel(in);
        }

        @Override
        public SongDetailsModel[] newArray(int size) {
            return new SongDetailsModel[size];
        }
    };

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(song);
        dest.writeString(url);
        dest.writeString(artists);
        dest.writeString(coverImage);
    }
}


