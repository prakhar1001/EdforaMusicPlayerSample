package prakhar.com.edforamusicsample.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import prakhar.com.edforamusicsample.R;

/**
 * Created by lendingkart on 3/12/2017.
 */

public class NoInternetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nointernet, container, false);
        return view;
    }
}
