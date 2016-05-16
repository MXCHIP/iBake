package com.mxchip.activities.ibake;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.mico.micosdk.MiCODevice;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/18.
 */
public class QrCodeActivity extends AppCompatActivity {
    private String TAG = "---QrCodeActivity---";
    private SetTitleBar stb;
    private MiCODevice micoDev = null;

    private Context context;
    private ImageView dev_qrcode_img;
    private ImageView qrcodeimg;
    private TextView dev_qrcode_name;

    private String deviceid;
    private String devicepw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_dev_qrcode);

        context = QrCodeActivity.this;
        micoDev = new MiCODevice(QrCodeActivity.this);

        stb = new SetTitleBar(QrCodeActivity.this);
        stb.setTitleName("DEVICE SHARING");
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        deviceid = (String) getIntent().getSerializableExtra("deviceid");
        devicepw = (String) getIntent().getSerializableExtra("devicepw");

        initView();
        getQrCode();
    }

    private void initView() {
        dev_qrcode_img = (ImageView) findViewById(R.id.dev_qrcode_imgid);
        qrcodeimg = (ImageView) findViewById(R.id.qrcodeimg);
        dev_qrcode_name = (TextView) findViewById(R.id.dev_qrcode_nametvid);

        String devname = (String) getIntent().getSerializableExtra("devname");
        dev_qrcode_name.setText(devname);
        byte[] imgbyte = (byte[]) getIntent().getSerializableExtra("imgbyte");
        dev_qrcode_img.setImageBitmap(ConstHelper.Bytes2Bitmap(imgbyte));
    }

    private void getQrCode() {
        SharePreHelper shareph = new SharePreHelper(context);
        String token = shareph.getData(ConstPara.SHARE_TOKEN);
        Log.d(TAG, token);

        micoDev.getShareVerCode(deviceid, new ManageDeviceCallBack() {

            @Override
            public void onSuccess(String message) {
                String qrcodemsg = "{\"deviceid\":\"" + deviceid + "\",\"devicepw\":\"" + devicepw + "\",\"vercode\":\"" + message + "\"}";
                qrcodeimg.setImageBitmap(micoDev.creatQrCode(qrcodemsg, 250, 250));
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, message);
                ConstHelper.setToast(QrCodeActivity.this, ConstHelper.getFogMessage(message));
            }
        }, token);
    }
}
