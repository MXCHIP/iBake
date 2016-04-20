package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/15.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private String TAG = "---SplashScreenActivity---";
    private SharePreHelper shareph;
    private MiCOUser micoUser = new MiCOUser();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                checkToken();
            }
        }, 500);
    }

    private void checkToken() {
        shareph = new SharePreHelper(SplashScreenActivity.this);

        if(ConstHelper.checkPara(shareph.getData(ConstPara.SHARE_GUIDE))){
            if (ConstHelper.checkPara(shareph.getData(ConstPara.SHARE_TOKEN))) {
                refreshToken(shareph.getData(ConstPara.SHARE_TOKEN));
            } else {
                toLoginPage();
            }
        }else{
            Intent intent = new Intent(SplashScreenActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        }


        /**
         * ces
         */
//        Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    private void refreshToken(String token) {
        micoUser.refreshToken(token, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.v(TAG + "refreshToken", message);

                if (ConstHelper.checkPara(ConstHelper.getFogErr(message))) {
                    ConstHelper.setToast(SplashScreenActivity.this, ConstHelper.getFogErr(message));
                    toLoginPage();
                } else if (ConstHelper.checkPara(ConstHelper.getFogToken(message))) {
                    shareph.addData(ConstPara.SHARE_TOKEN, ConstHelper.getFogToken(message));
                    Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.v(TAG + "refreshToken", message);
                toLoginPage();
            }
        });

    }

    private void toLoginPage() {
        Intent intent = new Intent(SplashScreenActivity.this, IBakeLoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
