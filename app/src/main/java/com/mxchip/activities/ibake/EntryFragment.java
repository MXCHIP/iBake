package com.mxchip.activities.ibake;

/**
 * Created by Rocke on 2016/04/06.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mxchip.manage.SharePreHelper;

public class EntryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, null);
        v.findViewById(R.id.btn_entry).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("---emter---", "OK");

                        SharePreHelper shareph = new SharePreHelper(getContext());
                        shareph.addData("guide", "1");

                        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });


        return v;
    }
}