package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.MiCOCallBack;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;

/**
 * Created by Rocke on 2016/03/10.
 */
public class ResetPasswordActivity extends AppCompatActivity {
    private String TAG = "---ResetPasswordActivity---";

    private String _APPID = ConstPara._APPID;
    private MiCOUser micoUser = new MiCOUser();
    private SetTitleBar stb;

    private Button rsp_vercodebtn;
    private EditText rsp_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_resetpw);

        stb = new SetTitleBar(ResetPasswordActivity.this);
        stb.setTitleName("RESET PASSWORD");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    private void initView() {
        rsp_vercodebtn = (Button) findViewById(R.id.rsp_getvercode_btnid);
        rsp_username = (EditText) findViewById(R.id.rsp_username_etid);
    }

    private void initOnClick() {
        rsp_vercodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = rsp_username.getText().toString().trim();
                Log.d(TAG, userName);

                micoUser.getVerifyCode(userName, _APPID, new MiCOCallBack() {

                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, message.toString());

                        if (ConstHelper.checkPara(ConstHelper.getFogCode(message))) {
                            if (ConstPara._SUCCESSCODE.equals(ConstHelper.getFogCode(message))) {

                                /**
                                 * 重置密码时候获取到验证码后跳到重新登录的界面
                                 */
                                Intent intent = new Intent(ResetPasswordActivity.this, SetPswCheckvercodeActivity.class);
                                intent.putExtra("username", userName);
                                startActivity(intent);
                                ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                                ama.addDestoryActivity(ResetPasswordActivity.this, ConstPara.RSPSW_PAGE);
                            } else {
                                ConstHelper.setToast(ResetPasswordActivity.this, ConstHelper.getFogMessage(message));
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG, message.toString());
                        ConstHelper.setToast(ResetPasswordActivity.this, message);
                    }
                });
            }
        });
    }
}
