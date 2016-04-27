package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/09.
 */
public class SetPasswordActivity extends AppCompatActivity {

    private String TAG = "---SetPasswordActivity---";

    private MiCOUser micoUser = new MiCOUser();
    private SetTitleBar stb;
    private SharePreHelper shareph;

    private EditText password;

    private EditText passwordcf;
    private TextView usernametv;
    private Button setpasswordbtn;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setpassword);

        String typeStr = (String) getIntent().getSerializableExtra("type");
        stb = new SetTitleBar(SetPasswordActivity.this);
        if(ConstPara.INTENT_TYPE_RSPSW.equals(typeStr)){
            stb.setTitleName(ConstPara.TITLENAME_RESETPASSWORD);
            stb.setLeftButton("back", "finish");
            stb.setRightButton("none", "");
        }else if(ConstPara.INTENT_TYPE_RG.equals(typeStr)){
            stb.setTitleName("SET PASSWORD");
            stb.setLeftButton("none", "");
            stb.setRightButton("skip", "finish");
        }

        initView();
        initOnClick();

        token = (String) getIntent().getSerializableExtra("token");
        shareph = new SharePreHelper(SetPasswordActivity.this);
    }

    private void initView() {
        password = (EditText) findViewById(R.id.sp_psw_etid);
        passwordcf = (EditText) findViewById(R.id.sp_psw_cf_etid);
        setpasswordbtn = (Button) findViewById(R.id.sp_psw_btnid);
        usernametv = (TextView) findViewById(R.id.sp_username_tvid);

        usernametv.setText((String) getIntent().getSerializableExtra("username"));
    }

    public void initOnClick() {
        setpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password1 = password.getText().toString().trim();
                String password2 = passwordcf.getText().toString().trim();

                micoUser.register(password1, password2, new UserCallBack() {

                    @Override
                    public void onSuccess(String message) {

                        if (ConstHelper.checkPara(ConstHelper.getFogCode(message))) {
                            if (ConstPara._SUCCESSCODE.equals(ConstHelper.getFogCode(message))) {
                                shareph.addData(ConstPara.SHARE_MQTTPW, password1);
                                /**
                                 * 注册成功后跳到重新登录的界面
                                 */
                                Intent intent = new Intent(SetPasswordActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();
                                ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                                ama.destoryActivity(ConstPara.RSPSW_PAGE);
                                ama.destoryActivity(ConstPara.INDEX_PAGE);
                                ama.destoryActivity(ConstPara.RSPSW_CKCODE_PAGE);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            } else {
                                ConstHelper.setToast(SetPasswordActivity.this, ConstHelper.getFogMessage(message));
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG, message);
                        ConstHelper.setToast(SetPasswordActivity.this, code + " message:" + message.toString());
                    }
                }, token);
            }
        });
    }
}
