package com.mxchip.activities.ibake;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mico.micosdk.MiCODevice;
import com.mico.micosdk.MiCOUser;
import com.mingle.widget.ShapeLoadingDialog;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/04.
 */
public class AddDevSuccessActivity extends AppCompatActivity {

    private String TAG = "---AddDevNextActivity---";
    private SetTitleBar stb;

    private Context context;
    private MiCODevice micoDev;
    private SharePreHelper shareph;

    private Button submit_new_devname;
    private EditText devname;

    private ShapeLoadingDialog shapeLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dev_success);

        context = AddDevSuccessActivity.this;

        stb = new SetTitleBar(AddDevSuccessActivity.this);
        stb.setTitleName("UPDATE NAME");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        micoDev = new MiCODevice(AddDevSuccessActivity.this);

        initView();
        initOnClick();
        shareph = new SharePreHelper(context);
    }

    private void initView() {
        submit_new_devname = (Button) findViewById(R.id.submit_newdevname_btn);
        devname = (EditText) findViewById(R.id.submit_newdevname_etid);

        String devnamestr = (String) getIntent().getSerializableExtra("devname");
        if(ConstHelper.checkPara(devnamestr)){
            devname.setText(devnamestr);
        }
    }

    private void initOnClick() {
        submit_new_devname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                String deviceid = (String) getIntent().getSerializableExtra("deviceid");
                String alias = devname.getText().toString().trim();
                String token = shareph.getData("token");
                micoDev.updateDeviceAlias(deviceid, alias, new ManageDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        shapeLoadingDialog.dismiss();
                        Log.d(TAG, ConstHelper.getFogData(message));

                        if (ConstPara._SUCCESSCODE.equals(ConstHelper.getFogCode(message))) {

                            /**
                             * 绑定成功后刷新
                             */
                            ConstHelper.setToast(AddDevSuccessActivity.this, ConstHelper.getFogMessage(message));
                            finish();
                        } else {
                            ConstHelper.setToast(AddDevSuccessActivity.this, ConstHelper.getFogMessage(message));
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        shapeLoadingDialog.dismiss();
                        Log.d(TAG, ConstHelper.getFogData(message));
                    }
                }, token);
            }
        });
    }

    private void showLoading() {
        shapeLoadingDialog = new ShapeLoadingDialog(this);
        shapeLoadingDialog.show();
        shapeLoadingDialog.setLoadingText("LOADING...");
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
    }
}