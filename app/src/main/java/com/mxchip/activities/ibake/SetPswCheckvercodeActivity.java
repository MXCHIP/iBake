package com.mxchip.activities.ibake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/10.
 */
public class SetPswCheckvercodeActivity extends AppCompatActivity {

    private String TAG = "---SetPswCheckvercodeActivity---";

    private Context context;

    private String _APPID = ConstPara._APPID;
    private MiCOUser micoUser = new MiCOUser();
    private SetTitleBar stb;
    private SharePreHelper shareph;

    private Button checkvercodebtn;
    private Button regetvercodebtn;
    private EditText rsp_vercode;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_rsp_vercode);

        context = SetPswCheckvercodeActivity.this;
        shareph = new SharePreHelper(SetPswCheckvercodeActivity.this);

        stb = new SetTitleBar(SetPswCheckvercodeActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_RESETPASSWORD);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        userName = (String) getIntent().getSerializableExtra("username");

        initView();
        initOnClick();
    }

    private void initView() {
        regetvercodebtn = (Button) findViewById(R.id.rsp_regetvercode_btn);
        checkvercodebtn = (Button) findViewById(R.id.rsp_checkvercode_btnid);
        rsp_vercode = (EditText) findViewById(R.id.rsp_vercode_etid);
    }

    private void initOnClick() {
        regetvercodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                micoUser.getVerifyCode(userName, _APPID, new UserCallBack() {

                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, message.toString());

                        if (ConstHelper.checkPara(ConstHelper.getFogMessage(message))) {
                            ConstHelper.setToast(context, ConstHelper.getFogMessage(message));
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG, message.toString());
                        ConstHelper.setToast(context, message);
                    }
                });
            }
        });
        checkvercodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String vercoden =  rsp_vercode.getText().toString().trim();
                micoUser.checkVerifyCode(userName, vercoden, _APPID,
                        new UserCallBack() {

                            @Override
                            public void onSuccess(String message) {
                                Log.d(TAG, message);
                                if (ConstHelper.checkPara(ConstHelper.getFogMessage(message))) {
                                    ConstHelper.setToast(context, ConstHelper.getFogMessage(message));

                                } else if (ConstHelper.checkPara(ConstHelper.getFogToken(message))) {
                                    shareph.addData("username", userName);
                                    shareph.addData("enduserid", ConstHelper.getFogEndUserid(message));
                                    shareph.addData("mqttpw", vercoden);
                                    shareph.addData("token", ConstHelper.getFogToken(message));

                                    // 跳转去注册界面并把username传递过去
                                    Intent intent = new Intent(context, SetPasswordActivity.class);
                                    intent.putExtra("username", userName);
                                    intent.putExtra("type", ConstPara.INTENT_TYPE_RSPSW);
                                    intent.putExtra("token", ConstHelper.getFogToken(message));
                                    startActivity(intent);
                                    ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                                    ama.addDestoryActivity(SetPswCheckvercodeActivity.this, ConstPara.RSPSW_CKCODE_PAGE);
                                }
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                Log.d(TAG, message);
                                ConstHelper.setToast(context, message);
                            }
                        });
            }
        });
    }
}
