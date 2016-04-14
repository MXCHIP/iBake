package com.mxchip.activities.ibake;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

public class IBakeLoginActivity extends FragmentActivity implements
        OnPageChangeListener, OnCheckedChangeListener {

    private String TAG = "---IBakeLoginActivity---";

    private ViewPager pager;
    private MainPageAdapter adapter;
    private List<Fragment> fragments;
    private RadioGroup group;
    private RadioButton logintab, registertab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.ibake_login);

        initLoginpage();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
		Log.v(TAG, "onPageScrollStateChanged");
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
		 Log.v(TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int arg0) {
		Log.v(TAG, "onPageSelected");
        getTabState(arg0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.logintab:
                pager.setCurrentItem(0);
                break;
            case R.id.registertab:
                pager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    private void getTabState(int index) {
        logintab.setChecked(false);
        registertab.setChecked(false);

        switch (index) {
            case 0:
                logintab.setChecked(true);
                break;
            case 1:
                registertab.setChecked(true);
                break;
            default:
                break;
        }

    }

    private void initLoginpage() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_LoginActivity());
        fragments.add(new Fragment_RegisterActivity());

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size() - 1);
        pager.setOnPageChangeListener(this);

        group = (RadioGroup) findViewById(R.id.radioGroup1);
        logintab = (RadioButton) findViewById(R.id.logintab);
        registertab = (RadioButton) findViewById(R.id.registertab);
        group.setOnCheckedChangeListener(this);
    }
}
