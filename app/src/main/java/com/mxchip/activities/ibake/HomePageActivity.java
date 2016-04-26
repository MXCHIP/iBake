package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.helpers.GetDelicacy;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "---HomePageActivity---";
    private SetTitleBar stb;

    private ImageView adddeviceimgid;
    private NavigationView navigationView;

    private SharePreHelper shareph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        stb = new SetTitleBar(HomePageActivity.this);
        stb.setTitleName("Welcome");
        stb.setLeftButton("drawer", "drawer");
        stb.setRightButton("cloud", "cloud");

        shareph = new SharePreHelper(HomePageActivity.this);

        initView();
        initOnClick();
    }

    //    初始化view
    private void initView() {
        adddeviceimgid = (ImageView) findViewById(R.id.adddevice_imgid);
        navigationView = (NavigationView) findViewById(R.id.homepage_nav_view);

        initDelicacy();
    }

    private void initDelicacy(){
        GetDelicacy gdc = new GetDelicacy(HomePageActivity.this);
        gdc.refreshDelicy();
    }

    //    初始化click事件
    private void initOnClick() {
        navigationView.setNavigationItemSelectedListener(this);
        stb.initNavHeaderLayOnClick(navigationView);

        adddeviceimgid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddDevNextActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map;

        for (int i = 0; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("title", "Delicious crisp bread " + i);
            map.put("info", "google " + i);
            map.put("img", R.drawable.empty_recipe);
            list.add(map);
        }
        return list;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        stb.commonOnNavItemSelected(item);
        return true;
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ConstHelper.setToast(this, ConstPara._AGAINFINISH);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}