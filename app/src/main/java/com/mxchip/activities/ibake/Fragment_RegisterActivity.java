package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SharePreHelper;

public class Fragment_RegisterActivity extends Fragment implements View.OnClickListener {

    private String TAG = "---Fragment_RegisterActivity---";

    private String _APPID = ConstPara._APPID;
    private MiCOUser micoUser = new MiCOUser();

    private SharePreHelper shareph;

    private Button getvercodebtn;
    private Button signupbtn;
    private EditText username;
    private EditText vercode;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shareph = new SharePreHelper(getActivity());

        view = inflater.inflate(R.layout.fragment_register, container, false);
        initView();
        onClick(getView());
        return view;
    }

    private void initView() {
        getvercodebtn = (Button) view.findViewById(R.id.rg_getvercode_btn);
        signupbtn = (Button) view.findViewById(R.id.rg_signup_btn);
        username = (EditText) view.findViewById(R.id.rg_username_etid);
        vercode = (EditText) view.findViewById(R.id.rg_vercode_etid);
    }

    String userName;

    @Override
    public void onClick(View v) {
        getvercodebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = username.getText().toString().trim();
                Log.d(TAG, userName + _APPID);
                micoUser.getVerifyCode(userName, _APPID, new UserCallBack() {

                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, message.toString());
                        ConstHelper.setToast(getActivity(), ConstHelper.getFogMessage(message));
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG, message.toString());
                        ConstHelper.setToast(getActivity(), message);
                    }
                });
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String vercoden = vercode.getText().toString().trim();
                micoUser.checkVerifyCode(userName, vercoden, _APPID,
                        new UserCallBack() {

                            @Override
                            public void onSuccess(String message) {
                                Log.d(TAG, message);
                                if (ConstHelper.checkPara(ConstHelper.getFogMessage(message))) {
                                    ConstHelper.setToast(getActivity(), ConstHelper.getFogMessage(message));

                                } else if (ConstHelper.checkPara(ConstHelper.getFogToken(message))) {
                                    shareph.addData(ConstPara.SHARE_USERNAME, userName);
                                    shareph.addData(ConstPara.SHARE_ENDERUSERID, ConstHelper.getFogEndUserid(message));
                                    shareph.addData(ConstPara.SHARE_MQTTPW, vercoden);
                                    shareph.addData(ConstPara.SHARE_TOKEN, ConstHelper.getFogToken(message));

                                    // 跳转去注册界面并把username传递过去
                                    Intent intent = new Intent(v.getContext(), SetPasswordActivity.class);
                                    intent.putExtra("username", userName);
                                    intent.putExtra("type", ConstPara.INTENT_TYPE_RG);
                                    intent.putExtra("token", ConstHelper.getFogToken(message));
                                    startActivity(intent);
                                    ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                                    ama.addDestoryActivity(getActivity(), ConstPara.INDEX_PAGE);
                                }
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                Log.d(TAG, message);
                                ConstHelper.setToast(getActivity(), message);
                            }
                        });
            }
        });
    }
}
