package com.mxchip.activities.ibake;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mico.micosdk.MiCODevice;
import com.mxchip.manage.SetTitleBar;

/**
 * Created by Rocke on 2016/03/14.
 */
public class AddDevRetryActivity extends AppCompatActivity {

    private String TAG = "---AddDevSSIDActivity---";
    private SetTitleBar stb;
    private Context context;
    public MiCODevice micodev;

    private Button retrybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dev_retry);

        context = AddDevRetryActivity.this;
        micodev = new MiCODevice(context);

        stb = new SetTitleBar(AddDevRetryActivity.this);
        stb.setTitleName("FAILURE");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    private void initView() {
        retrybtn = (Button) findViewById(R.id.retryeasylink_btnid);
    }

    private void initOnClick() {
        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}