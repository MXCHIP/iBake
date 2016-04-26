package com.mxchip.activities.ibake;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.dtr.zxing.bean.ComminuteCode;
import com.mico.micosdk.MiCODevice;
import com.mico.micosdk.MiCOUser;
import com.mingle.widget.ShapeLoadingDialog;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.helper.ShareDeviceParams;
import com.mxchip.helpers.BookItemAdapter;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by Rocke on 2016/03/07.
 */
public class MyDeviceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, WaveSwipeRefreshLayout.OnRefreshListener {

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private String TAG = "---MyDeviceActivity---";
    private static final int REFRESH_LIST = 0x10001;

    private SetTitleBar stb;

    private BookItemAdapter adapter;
    private MiCOUser micoUser = new MiCOUser();
    private MiCODevice micoDev;
    private ShapeLoadingDialog shapeLoadingDialog;

    private ListView mydevlistlistviewid;
    private Button mydev2_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_dev_list);

        micoDev = new MiCODevice(MyDeviceActivity.this);

        stb = new SetTitleBar(MyDeviceActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_MYDEVICE);
        stb.setLeftButton("back", "homepage");
        stb.setRightButton("scan", "scan");

        initView();
        initDevList();
        initOnClick();
    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.mydev_list_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(Color.argb(0xC8, 0xFC, 0xCB, 0x0F));// 200 FCCB0F
        mWaveSwipeRefreshLayout.setMaxDropHeight(500);

        mydevlistlistviewid = (ListView) findViewById(R.id.mydevlistlistviewid);
        mydev2_add = (Button) findViewById(R.id.mydev2_add_btn);

        showLoading();
    }

    private void initOnClick(){
        mydev2_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDeviceActivity.this, AddDevNextActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void showLoading() {
        shapeLoadingDialog = new ShapeLoadingDialog(this);
        shapeLoadingDialog.show();
        shapeLoadingDialog.setLoadingText("LOADING...");
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
    }

    private void initDevList() {

        adapter = new BookItemAdapter(MyDeviceActivity.this, mydevlistlistviewid);
        mydevlistlistviewid.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG + "onItemClick", position + " parent");
//        Log.d(TAG + "onItemClick", view.getId() + " view id");
//        Log.d(TAG + "onItemClick", view.getId() + " id");
//        Intent intent = new Intent(MyDeviceActivity.this, DevCtrlActivity.class);
//        startActivity(intent);
//        finish();
//        ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
//        ama.destoryActivity(ConstPara.HOME_PAGE);
    }

    private MyHandler handler = new MyHandler();

    private void reload() {
        adapter.clean();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadDate();
                handler.sendEmptyMessage(REFRESH_LIST);
            }
        }).start();
    }

    public void loadDate() {
        SharePreHelper shareph = new SharePreHelper(MyDeviceActivity.this);
        String token = shareph.getData(ConstPara.SHARE_TOKEN);
        micoUser.getDeviceList(new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "getDeviceList", message);
                try {
                    JSONArray datas = new JSONArray(ConstHelper.getFogData(message));
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject temp = (JSONObject) datas.get(i);
                        String name = temp.getString("device_name");
                        String online = Boolean.parseBoolean(temp.getString("online")) ? "online" : "offline";
                        String img = temp.getString("product_icon");
                        String deviceid = temp.getString("device_id");
                        String devicepw = temp.getString("device_pw");
//                        Log.d(TAG + "getlist", name + ' ' + online + ' ' + img);
                        adapter.addBook(name, online, img, deviceid, devicepw);
                    }
                    mydevlistlistviewid.setAdapter(adapter);
                    shapeLoadingDialog.dismiss();
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
                shapeLoadingDialog.dismiss();
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, token);
    }


    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("---postDelayed---", "finish");
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onRefresh() {
//        refresh();
        reload();
    }

    private class MyHandler extends Handler {
        public static final int SHOW_STR_TOAST = 0;
        public static final int SHOW_RES_TOAST = 1;

        private String toast_message = null;
        private int toast_res;

        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case SHOW_STR_TOAST:
                        Log.d("---handleMessage---", toast_message);
                        break;
                    case SHOW_RES_TOAST:
                        Log.d("---handleMessage---", toast_res + "");
                        break;
                    case REFRESH_LIST:
                        Log.d("---handleMessage---", REFRESH_LIST + "");
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ComminuteCode.RESULT_CODE && !(null == data)) {
            Log.d(TAG, "my code is -->" + data.getStringExtra("qrresult"));
            String message = data.getStringExtra("qrresult");
            grantYourDevice(message);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void grantYourDevice(String message) {

        ShareDeviceParams sdevp = new ShareDeviceParams();
        SharedPreferences sharedPreferences = getSharedPreferences("fogcloud", Activity.MODE_PRIVATE);
        String bindingtype = ConstPara.BINDINGTYPE;
        int role = ConstPara.ROLE;

        try {
            JSONObject msgObj = new JSONObject(message);
            sdevp.deviceid = msgObj.getString("deviceid");
            sdevp.devicepw = msgObj.getString("devicepw");
            sdevp.bindvercode = msgObj.getString("vercode");
            sdevp.role = role;
            sdevp.bindingtype = bindingtype;
            sdevp.iscallback = false;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        micoDev.addDeviceByVerCode(sdevp, new ManageDeviceCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG, message);
                if (ConstHelper.checkPara(ConstHelper.getFogCode(message))) {
                    if (ConstPara._SUCCESSCODE.equals(ConstHelper.getFogCode(message))) {

                        /**
                         * 绑定成功后刷新
                         */
                        ConstHelper.setToast(MyDeviceActivity.this, ConstHelper.getFogMessage(message));
//                        reload();
                    } else {
                        ConstHelper.setToast(MyDeviceActivity.this, ConstHelper.getFogMessage(message));
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
                ConstHelper.setToast(MyDeviceActivity.this, ConstHelper.getFogErr(message));
            }
        }, sharedPreferences.getString("token", ""));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        reload();
    }
}
