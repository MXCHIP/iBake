package com.mxchip.activities.ibake;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mico.micosdk.MiCODevice;
import com.mxchip.callbacks.EasyLinkCallBack;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.callbacks.SearchDeviceCallBack;
import com.mxchip.helper.MiCOConstParam;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rocke on 2016/03/03.
 */
public class AddDevSSIDActivity extends AppCompatActivity {

    private String TAG = "---AddDevSSIDActivity---";
    private SetTitleBar stb;
    private Context context;
    public MiCODevice micodev;

    private Button searchdev;
    private EditText dev_ssid;
    private EditText dev_password;

    private boolean hasWifi = false;
    private String wifissid;
    private String password;

    private boolean easylinkTag = false;

    private ProgressDialog progressDlg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dev_ssid);

        context = AddDevSSIDActivity.this;
        micodev = new MiCODevice(context);

        stb = new SetTitleBar(AddDevSSIDActivity.this);
        stb.setTitleName("ADD DEVICE");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    //    初始化view
    private void initView() {
        dev_ssid = (EditText) findViewById(R.id.add_dev_ssid_etid);
        dev_password = (EditText) findViewById(R.id.add_dev_psw_etid);
        searchdev = (Button) findViewById(R.id.searchdev_btnid);

        if (ConstHelper.checkPara(micodev.getSSID())) {

            wifissid = micodev.getSSID();
            if (!wifissid.equals("<unknown ssid>")) {
                hasWifi = true;
                dev_ssid.setText(micodev.getSSID());
            } else {
                hasWifi = false;
                dev_ssid.setText(ConstPara.SSID_NAME_BODY);
            }
        }
    }

    //    初始化所有的参数
    private void initAppPara() {
        micodev = null;
        micodev = new MiCODevice(context);
        easylinkTag = true;
        retrypgtag = false;
    }

    //    初始化click事件
    private void initOnClick() {
        searchdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = dev_password.getText().toString().trim();
                Log.d(TAG, wifissid + " " + password);
                if (!easylinkTag) {
                    if (hasWifi) {
                        if (ConstHelper.checkPara(wifissid)) {

                            initAppPara();

                            //  打开searchdevice
                            startSearchDevices();
                            startEasyLink();

                        } else {
                            ConstHelper.setToast(context, ConstPara.PARA_EMPTY);
                        }
                    } else {
                        ConstHelper.setToast(context, ConstPara.SSID_NAME_BODY);
                    }
                } else {
                    ConstHelper.setToast(context, ConstPara.ISWORKING);
                }
            }
        });
    }

    // 工作线程
    private void startSearchDevices() {
        // 处理比较耗时的操作
        micodev.startSearchDevices(ConstPara.MDNS_SER_NAME, new SearchDeviceCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG + " mDNS", message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + " mDNS", message);
            }

            @Override
            public void onDevicesFind(JSONArray deviceStatus) {
                Log.d(TAG + " onDevicesFind", deviceStatus.toString());
                if (!deviceStatus.equals("")) {
                    getBindDevByIP(deviceStatus);
                }
            }
        });
    }

    private void getBindDevByIP(JSONArray tempmDNS) {
        if (null != tempmDNS) {
            JSONObject temp;
            JSONArray tempArr = tempmDNS;
            for (int i = 0; i < tempArr.length(); i++) {
                try {
                    temp = (JSONObject) tempArr.get(i);
//                    if (temp.getString("isHaveSuperUser").equals("0")) {
                    if (temp.getString("isHaveSuperUser").equals("false")) {
                        Log.d(TAG + " get bind IP", temp.getString("deviceIP"));

                        toBindDevice(temp.getString("deviceIP"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startEasyLink() {
        showEasylinkPro();
        micodev.startEasyLink(wifissid, password, 40000, 100, new EasyLinkCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG + " easylink", message);
                if ("stop success".equals(message)) {
                    toRetryPage();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + " easylink", code + " " + message);
            }
        }, "sin");
    }

    private void stopEasyLink() {
        easylinkTag = false;

        micodev.stopEasyLink(new EasyLinkCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG + " easylink", message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + " easylink", code + " " + message);
            }
        });
    }

    private void stopSearchDevices() {
        micodev.stopSearchDevices(new SearchDeviceCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG + " mDNS", message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + " mDNS", code + " " + message);
            }
        });
    }

    private void toBindDevice(String ip) {
        dismissEasyLinkPro();
        stopEasyLink();
        stopSearchDevices();

        SharePreHelper shareph = new SharePreHelper(context);

        String port = MiCOConstParam.LOCALDEVICEPORT;

        micodev.bindDevice(ip, port, new ManageDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + " bindDevice", message);

                if (ConstHelper.checkPara(ConstHelper.getFogCode(message))) {
                    if (ConstPara._SUCCESSCODE.equals(ConstHelper.getFogCode(message))) {

                        /**
                         * 绑定成功后跳到修改名称的界面
                         */
                        String binddata = ConstHelper.getFogData(message);
                        toSuccessPage(binddata);
                    } else {
                        ConstHelper.setToast(context, ConstHelper.getFogMessage(message));
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + " bindDevice", message);
//                toRetryPage();
            }
        }, shareph.getData(ConstPara.SHARE_TOKEN));
    }

    private void toSuccessPage(String data) {
        Log.d(TAG + "tosuccessPage", data);

        try {
            JSONObject datajson = new JSONObject(data);
            Intent intent = new Intent(AddDevSSIDActivity.this, AddDevSuccessActivity.class);
            intent.putExtra("deviceid", datajson.getString("deviceid"));
            intent.putExtra("devicepw", datajson.getString("devicepw"));
            intent.putExtra("devname", datajson.getString("devicename"));
            startActivity(intent);
            finish();
            ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
            ama.addDestoryActivity(AddDevSSIDActivity.this, ConstPara.SSID_PAGE);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    boolean retrypgtag = false;

    private void toRetryPage() {
        dismissEasyLinkPro();
        stopEasyLink();
        stopSearchDevices();
        if (!retrypgtag) {
            retrypgtag = true;
            Intent intent = new Intent(AddDevSSIDActivity.this, AddDevRetryActivity.class);
            startActivity(intent);
        }
    }

    private void showEasylinkPro() {
        progressDlg = new ProgressDialog(context);
        progressDlg.setTitle("EasyLink");
        progressDlg.setMessage("Send SSID and password to the device.");
        progressDlg.setIcon(R.drawable.logo_login_64);
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDlg.setCancelable(false);
        progressDlg.show();
    }

    private void dismissEasyLinkPro() {
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissEasyLinkPro();
        stopEasyLink();
        stopSearchDevices();
        finish();
    }
}
