package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Rocke on 2016/03/15.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private String TAG = "---SplashScreenActivity---";
    private SharePreHelper shareph;
    private String newToken;
    private String lastdeviceid;
    private MiCOUser micoUser = new MiCOUser();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        if (ConstHelper.checkPara(shareph.getData(ConstPara.SHARE_GUIDE))) {
            if (ConstHelper.checkPara(shareph.getData(ConstPara.SHARE_TOKEN))) {
                refreshToken(shareph.getData(ConstPara.SHARE_TOKEN));
            } else {
                toLoginPage();
            }
        } else {
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

                    newToken = ConstHelper.getFogToken(message);

                    shareph.addData(ConstPara.SHARE_TOKEN, newToken);

                    lastdeviceid = shareph.getData(ConstPara.SHARE_LASTDEVICEID);

                    if (ConstHelper.checkPara(lastdeviceid)) {
                        micoUser.getDeviceInfo(lastdeviceid, new UserCallBack() {
                            @Override
                            public void onSuccess(String message) {
                                Log.d(TAG + "getDeviceInfo", ConstHelper.getFogData(message));
                                try {
                                    JSONObject devinfo = new JSONObject(ConstHelper.getFogData(message));
                                    pageRouter("ctrldev", devinfo);
                                } catch (JSONException e) {
                                    pageRouter("homepage", null);
                                }
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                pageRouter("homepage", null);
                            }
                        }, newToken);
                    } else {
                        pageRouter("homepage", null);
                    }
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

    private void pageRouter(String pagetag, JSONObject devinfo) {

        initFilePath();

        Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
        switch (pagetag) {
            case "ctrldev":
                try {
                    if (Boolean.parseBoolean(devinfo.getString("online"))) {
                        intent = new Intent(SplashScreenActivity.this, DevCtrlActivity.class);
                        intent.putExtra(ConstPara.INTENT_DEVNAME, devinfo.getString("alias"));
                        intent.putExtra(ConstPara.INTENT_DEVID, lastdeviceid);
                        intent.putExtra(ConstPara.INTENT_DEVPW, devinfo.getString("devicepw"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        if (null != intent) {
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    private void initFilePath() {

        String filePath = "";
        if (externalMemoryAvailable()) {
            filePath = Environment.getExternalStorageDirectory() + "/iBakImgs/";
            ConstPara._FILE_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "";
        } else {
            filePath = Environment.getDataDirectory() + "/iBakImgs/";
            ConstPara._FILE_ROOT_PATH = Environment.getDataDirectory() + "";
        }

        Log.d(TAG, "initFilePath = " + ConstPara._FILE_ROOT_PATH);
        if (ConstHelper.checkPara(filePath)) {
            File f = new File(filePath);
            if (!f.exists()) {
                if (!f.mkdir()) {
                    f.mkdirs();
                }
            }
        }
    }

    /**
     * 手机是否存在SD卡
     *
     * @return
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
