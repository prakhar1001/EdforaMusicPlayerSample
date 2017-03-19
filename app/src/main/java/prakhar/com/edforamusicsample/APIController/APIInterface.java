package prakhar.com.edforamusicsample.APIController;

import java.util.List;

import prakhar.com.edforamusicsample.Model.SongDetailsModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lendingkart on 3/18/2017.
 */

public interface APIInterface {
//http://starlord.hackerearth.com/edfora/cokestudio
    String ENDPOINT = "http://starlord.hackerearth.com/";

    @GET("edfora/cokestudio")
    Call<List<SongDetailsModel>> GET_SONG_LIST();


}
