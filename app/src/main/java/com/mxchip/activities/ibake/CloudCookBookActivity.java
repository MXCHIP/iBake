package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;

/**
 * Created by Rocke on 2016/03/10.
 */
public class CloudCookBookActivity extends AppCompatActivity {
    private String TAG = "---AddDevNextActivity---";
    private SetTitleBar stb;
    private EditText cloud_cb_search;
    private ImageView cloud_cb_cleanbtn;
    private TextView cloud_cb_search_btn;
    private LinearLayout cloud_cb_cakely;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_cookbook);

        stb = new SetTitleBar(CloudCookBookActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_RECIPE);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    //    初始化view
    private void initView() {
        cloud_cb_search = (EditText) findViewById(R.id.cloud_cb_search_txt);
        cloud_cb_cleanbtn = (ImageView) findViewById(R.id.cloud_cb_cleanbtn);
        cloud_cb_search_btn = (TextView) findViewById(R.id.cloud_cb_search_btn);

        cloud_cb_cakely = (LinearLayout) findViewById(R.id.cloud_cb_cakely);
    }

    //    初始化click事件
    private void initOnClick() {
        cloud_cb_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cloud_cb_search_btn.setVisibility(View.VISIBLE);
                } else {
                    cloud_cb_cleanbtn.setVisibility(View.GONE);
                    cloud_cb_search_btn.setVisibility(View.GONE);
                }
            }
        });
        cloud_cb_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    cloud_cb_cleanbtn.setVisibility(View.VISIBLE);
                else
                    cloud_cb_cleanbtn.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cloud_cb_cleanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cloud_cb_search.setText("");
            }
        });

        cloud_cb_cakely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CloudCookBookActivity.this, RecipesActivity.class);
                intent.putExtra("recipename", ConstPara.TITLENAME_CAKE);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // if the imgview is gone getvisibility() is 0
            int isvisib = cloud_cb_cleanbtn.getVisibility();
            if ((0 == isvisib)) {
                cloud_cb_search.clearFocus();
                cloud_cb_search.setText("");
            } else {
                finish();
            }
        }
        return true;
    }
}
