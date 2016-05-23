package com.mxchip.fogcloud;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.MiCOConstParam;

/**
 * Send paraments by http
 *
 * @version 1.0
 * @Project MiCOSDK
 * @Author Sin
 * @Createtime
 */
public class HttpSendParam {
    private CommonFunc comfunc = new CommonFunc();

    // private String TAG = "---HttpSendParam---";

    public void doHttpPost(String httpsUrl, JSONObject param, final UserCallBack usercb, String... jwt) {

        HttpUtils http = new HttpUtils();
        http.configTimeout(MiCOConstParam._TIMEOUT);
        RequestParams params = new RequestParams();

        params.addHeader("Content-Type", "application/json");
        if (jwt.length > 0) {
            params.addHeader("Authorization", "JWT " + jwt[0]);
        }
        // params.addHeader("X-Application-Id",
        // "189cf0d5-4bd9-4d3f-a65d-342adbea735b");
        // params.addHeader("X-Request-Sign", ToolUtils.getRequestSign());
        // Log.i(TAG, "Request-Sign" + ToolUtils.getRequestSign());

        try {
            params.setBodyEntity(new StringEntity(param.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        /**
         * use HttpUtils send param to url
         */
        http.send(HttpRequest.HttpMethod.POST, httpsUrl, params,
                new RequestCallBack<String>() {

                    @Override
                    /**
                     * success
                     */
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        comfunc.succeesCBFilterUser(responseInfo.result, usercb);
                    }

                    @Override
                    /**
                     * error
                     */
                    public void onFailure(HttpException error, String msg) {
                        comfunc.failureCBFilterUser(error.getExceptionCode(), msg, usercb);
                    }
                });
    }

    public void doHttpPut(String httpsUrl, JSONObject param, final UserCallBack usercb, String... jwt) {

        HttpUtils http = new HttpUtils();
        http.configTimeout(MiCOConstParam._TIMEOUT);
        RequestParams params = new RequestParams();

        params.addHeader("Content-Type", "application/json");
        if (jwt.length > 0) {
            params.addHeader("Authorization", "JWT " + jwt[0]);
        }

        try {
            params.setBodyEntity(new StringEntity(param.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        /**
         * use HttpUtils send param to url
         */
        http.send(HttpRequest.HttpMethod.PUT, httpsUrl, params,
                new RequestCallBack<String>() {

                    @Override
                    /**
                     * success
                     */
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        comfunc.succeesCBFilterUser(responseInfo.result, usercb);
                    }

                    @Override
                    /**
                     * error
                     */
                    public void onFailure(HttpException error, String msg) {
                        comfunc.failureCBFilterUser(error.getExceptionCode(), msg, usercb);
                    }
                });
    }

    public void doHttpGet(String httpsUrl, String param, final UserCallBack usercb, String... jwt) {

        HttpUtils http = new HttpUtils();
        http.configTimeout(MiCOConstParam._TIMEOUT);
        RequestParams params = new RequestParams();

        params.addHeader("Content-Type", "application/json");
//        params.addHeader("Cache-Control","no-cache");
//        params.addHeader("Cache-Control", "no-store");

        if (jwt.length > 0) {
            params.addHeader("Authorization", "JWT " + jwt[0]);
        }

        httpsUrl += param;


        /**
         * use HttpUtils send param to url
         */
        http.send(HttpRequest.HttpMethod.GET, httpsUrl, params,
                new RequestCallBack<String>() {

                    @Override
                    /**
                     * success
                     */
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        comfunc.succeesCBFilterUser(responseInfo.result, usercb);
                    }

                    @Override
                    /**
                     * error
                     */
                    public void onFailure(HttpException error, String msg) {
                        comfunc.failureCBFilterUser(error.getExceptionCode(), msg, usercb);
                    }
                });
    }

    //delete
    public void doHttpDelete(String httpsUrl, String param, final UserCallBack usercb, String... jwt) {

        HttpUtils http = new HttpUtils();
        http.configTimeout(MiCOConstParam._TIMEOUT);
        RequestParams params = new RequestParams();

        params.addHeader("Content-Type", "application/json");
//        params.addHeader("Cache-Control","no-cache");
//        params.addHeader("Cache-Control", "no-store");

        if (jwt.length > 0) {
            params.addHeader("Authorization", "JWT " + jwt[0]);
        }

        httpsUrl += param;

        /**
         * use HttpUtils send param to url
         */
        http.send(HttpRequest.HttpMethod.DELETE, httpsUrl, params,
                new RequestCallBack<String>() {

                    @Override
                    /**
                     * success
                     */
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        comfunc.succeesCBFilterUser(responseInfo.result, usercb);
                    }

                    @Override
                    /**
                     * error
                     */
                    public void onFailure(HttpException error, String msg) {
                        comfunc.failureCBFilterUser(error.getExceptionCode(), msg, usercb);
                    }
                });
    }
}
