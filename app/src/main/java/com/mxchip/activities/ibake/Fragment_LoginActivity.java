package com.mxchip.activities.ibake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mico.micosdk.MiCOUser;
import com.mingle.widget.ShapeLoadingDialog;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ActivitiesManagerApplication;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SharePreHelper;

public class Fragment_LoginActivity extends Fragment implements OnClickListener {
    private String TAG = "---Fragment_LoginActivity---";

    private String _APPID = ConstPara._APPID;
    private MiCOUser micoUser = new MiCOUser();
    private ShapeLoadingDialog shapeLoadingDialog;

    private SharePreHelper shareph;

    private Button loginbtn;
    private EditText usernameid;
    private EditText passwordid;
    private LinearLayout forget_layout;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shareph = new SharePreHelper(getActivity());

        view = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        onClick(getView());
        return view;
    }

    private void initView() {
        loginbtn = (Button) view.findViewById(R.id.login_btn);
        usernameid = (EditText) view.findViewById(R.id.username_etid);
        passwordid = (EditText) view.findViewById(R.id.password_etid);
        forget_layout = (LinearLayout) view.findViewById(R.id.login_forget_layoutid);

        if (ConstHelper.checkPara(shareph.getData(ConstPara.SHARE_USERNAME))) {
            usernameid.setText(shareph.getData(ConstPara.SHARE_USERNAME));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        loginbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                showLoading();

                final String userName = usernameid.getText().toString();
                final String password = passwordid.getText().toString();

                micoUser.login(userName, password, _APPID,
                        new UserCallBack() {
                            @Override
                            public void onSuccess(String message) {
                                Log.d(TAG, message.toString());

                                if (ConstHelper.checkPara(ConstHelper.getFogErr(message))) {
                                    ConstHelper.setToast(getActivity(), ConstHelper.getFogErr(message));
                                } else if (ConstHelper.checkPara(ConstHelper.getFogToken(message))) {
                                    shareph.addData(ConstPara.SHARE_USERNAME, userName);
                                    shareph.addData(ConstPara.SHARE_ENDERUSERID, ConstHelper.getFogEndUserid(message));
                                    shareph.addData(ConstPara.SHARE_MQTTPW, password);
                                    shareph.addData(ConstPara.SHARE_TOKEN, ConstHelper.getFogToken(message));
                                    shapeLoadingDialog.dismiss();
                                    // 跳转到登录成功的首页
                                    Intent intent = new Intent(v.getContext(), HomePageActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                }
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                Log.d(TAG, code + " message:" + message.toString());
                                shapeLoadingDialog.dismiss();
                                ConstHelper.setToast(getActivity(), message.toString());
                            }
                        });
            }
        });

        forget_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到重置密码的界面
                Intent intent = new Intent(v.getContext(), ResetPasswordActivity.class);
                startActivity(intent);
                ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                ama.addDestoryActivity(getActivity(), ConstPara.INDEX_PAGE);
            }
        });
    }

    private void showLoading(){
        shapeLoadingDialog = new ShapeLoadingDialog(getActivity());
        shapeLoadingDialog.show();
        shapeLoadingDialog.setLoadingText("LOADING...");
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
    }
}
