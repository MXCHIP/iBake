package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mxchip.manage.SetTitleBar;

/**
 * Created by Rocke on 2016/03/04.
 * <p/>
 * Open Activity: startmDNS
 */
public class AddDevNextActivity extends AppCompatActivity {

    private String TAG = "---AddDevNextActivity---";
    private SetTitleBar stb;
    private Button easylink_nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dev_next);

        stb = new SetTitleBar(AddDevNextActivity.this);
        stb.setTitleName("ADD DEVICE");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    //    初始化view
    private void initView() {
        easylink_nextbtn = (Button) findViewById(R.id.easylink_next_btnid);
    }

    //    初始化click事件
    private void initOnClick() {
        easylink_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
    }

    private void nextStep() {
        Intent intent = new Intent(AddDevNextActivity.this, AddDevSSIDActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}