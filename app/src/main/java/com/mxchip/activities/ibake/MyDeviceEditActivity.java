package com.mxchip.activities.ibake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mico.micosdk.MiCODevice;
import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.helpers.MyDeviceUserAdapter;
import com.mxchip.manage.ActionSheetDialog;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rocke on 2016/03/07.
 */
public class MyDeviceEditActivity extends AppCompatActivity {

    private String TAG = "---MyDeviceEditActivity---";
    private SetTitleBar stb;
    private MiCOUser micoUser = new MiCOUser();

    private Context context;

    private ListView edit_dev_user_lvid;
    private TextView dev_detail_name;
    private ImageView device_detail_imgid;
    private ImageView devname_editimg;
    private LinearLayout dev_detail_share;
    private Button mydevedit2_ctrl;
    private LinearLayout device_item_ly;

    private String deviceid;
    private String devicepw;
    private String devname;
    private byte[] imgbyte;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_dev_edit);

        context = MyDeviceEditActivity.this;

        stb = new SetTitleBar(MyDeviceEditActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_MYDEVICE);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("delete", "");

        deviceid = (String) getIntent().getSerializableExtra("deviceid");
        devicepw = (String) getIntent().getSerializableExtra("devicepw");
        initView();
        initOnClick();

        SharePreHelper shareph = new SharePreHelper(context);
        token = shareph.getData(ConstPara.SHARE_TOKEN);
    }


    private void initView() {
        edit_dev_user_lvid = (ListView) findViewById(R.id.edit_dev_user_lvid);
        dev_detail_name = (TextView) findViewById(R.id.dev_detail_nametvid);
        device_detail_imgid = (ImageView) findViewById(R.id.device_detail_imgid);
        devname_editimg = (ImageView) findViewById(R.id.devname_editimgid);
        dev_detail_share = (LinearLayout) findViewById(R.id.dev_detail_sharelyid);
        mydevedit2_ctrl = (Button) findViewById(R.id.mydevedit2_ctrl_btn);
        device_item_ly = (LinearLayout) findViewById(R.id.device_item_lyid);

        imgbyte = (byte[]) getIntent().getSerializableExtra("deviceimg");
        device_detail_imgid.setImageBitmap(ConstHelper.Bytes2Bitmap(imgbyte));

        devname = (String) getIntent().getSerializableExtra("devicename");
        dev_detail_name.setText(devname);
    }

    private void initOnClick() {
        dev_detail_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDeviceEditActivity.this, QrCodeActivity.class);
                intent.putExtra("deviceid", deviceid);
                intent.putExtra("devicepw", devicepw);
                intent.putExtra("imgbyte", imgbyte);
                intent.putExtra("devname", devname);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        devname_editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDeviceEditActivity.this, AddDevSuccessActivity.class);
                intent.putExtra("deviceid", deviceid);
                intent.putExtra("devname", devname);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        stb.getRightToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "---delete");
                // TODO 解绑设备
                new ActionSheetDialog(context)
                        .builder()
                        .setTitle("Renounce the use of the device permissions?")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("Remove", ActionSheetDialog.SheetItemColor.Yellow,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.d(TAG, "to ----- removeMyDevice");
                                        removeMyDevice();
                                    }
                                }).show();
            }
        });

        mydevedit2_ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "to ----- homepage activity");

            }
        });

    }

    private void getDeviceInfo() {

        micoUser.getDeviceInfo(deviceid, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "getDeviceInfo", ConstHelper.getFogData(message));
                try {
                    JSONObject devinfo = new JSONObject(ConstHelper.getFogData(message));
                    String aliasTmp = devinfo.getString("alias");
                    if (ConstHelper.checkPara(aliasTmp)) {
                        devname = devinfo.getString("alias");
                        dev_detail_name.setText(devname);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
            }
        }, token);
    }

    private void setAdapter(String message) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map;

        try {
            JSONArray datas = new JSONArray(ConstHelper.getFogData(message));
            JSONObject temp;
            String nickname = "Human";
            String phone = "";
            for (int i = 0; i < datas.length(); i++) {
                temp = (JSONObject) datas.get(i);
                map = new HashMap<String, Object>();

                if (ConstHelper.checkPara(temp.getString("realname"))) {
                    nickname = temp.getString("realname");
                } else if (ConstHelper.checkPara(temp.getString("nickname"))) {
                    nickname = temp.getString("nickname");
                }

                if (ConstHelper.checkPara(temp.getString("phone"))) {
                    phone = temp.getString("phone");
                } else if (ConstHelper.checkPara(temp.getString("email"))) {
                    phone = temp.getString("email");
                }

                map.put("nickname", nickname);
                map.put("loginname", phone);
                map.put("enduserid", temp.getString("enduserid"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyDeviceUserAdapter adapter = new MyDeviceUserAdapter(MyDeviceEditActivity.this,
                list,
                R.layout.my_dev_edit_item_user,
                new String[]{"nickname", "loginname", "enduserid"},
                new int[]{R.id.mydev_user_txid, R.id.mydev_user_phone_txid, R.id.mydev_user_rm_imgid}, deviceid);

        edit_dev_user_lvid.setAdapter(adapter);
    }

    private void getMemberList() {

        micoUser.getMemberList(deviceid, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "getMemberList", message);
                if(!"[]".equals(ConstHelper.getFogData(message))){
                    device_item_ly.setVisibility(View.VISIBLE);
                    setAdapter(message);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
            }
        }, token);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        getDeviceInfo();
        getMemberList();
    }

    private void removeMyDevice() {
        MiCODevice micoDev = new MiCODevice(context);
        micoDev.unBindDevice(deviceid, new ManageDeviceCallBack() {

            @Override
            public void onSuccess(String message) {
                Log.d(TAG, message);
                ConstHelper.setToast(MyDeviceEditActivity.this, ConstHelper.getFogMessage(message));
                finish();
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
            }
        }, token);
    }
}

