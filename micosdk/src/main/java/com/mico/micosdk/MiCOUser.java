package com.mico.micosdk;

import com.mxchip.callbacks.UserCallBack;
import com.mxchip.fogcloud.HttpSendParam;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Account's implements, includes login, register...
 * Project:MiCOSDK
 * Author:Sin
 * Create time:20160120
 *
 * @version 1.0
 */
public class MiCOUser {

    private HttpSendParam hsp = new HttpSendParam();
    private CommonFunc comfunc = new CommonFunc();

    /**
     * Get phone verify code.
     *
     * @param loginname
     * @param appid
     * @param usercb
     */
    public void getVerifyCode(String loginname, String appid, UserCallBack usercb) {
        if (comfunc.checkPara(loginname, appid)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("loginname", loginname);
                postParam.put("appid", appid);
                hsp.doHttpPost(Configuration.GETVERCODE, postParam, usercb);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Vrify phone SMS code.
     *
     * @param loginname
     * @param vercode
     * @param appid
     * @param usercb
     */
    public void checkVerifyCode(String loginname, String vercode, String appid, UserCallBack usercb) {
        if (comfunc.checkPara(loginname, vercode, appid)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("loginname", loginname);
                postParam.put("vercode", vercode);
                postParam.put("appid", appid);
                hsp.doHttpPost(Configuration.CHECKVERCODE, postParam, usercb);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Register my account to fogcloud,
     * set my initial password
     *
     * @param password1
     * @param password2
     * @param usercb
     * @param token
     */
    public void register(String password1, String password2, UserCallBack usercb, String token) {
        if (comfunc.checkPara(password1, password2, token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("password1", password1);
                postParam.put("password2", password2);
                hsp.doHttpPost(Configuration.RESETPASSWORD, postParam, usercb, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Login my account.
     *
     * @param loginname
     * @param password
     * @param appid
     * @param usercb
     */
    public void login(String loginname, String password, String appid,
                      UserCallBack usercb) {
        if (comfunc.checkPara(loginname, password, appid)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("loginname", loginname);
                postParam.put("password", password);
                postParam.put("appid", appid);
                hsp.doHttpPost(Configuration.LOGININ, postParam, usercb);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Refresh my token, and let it delay another 7 days.
     *
     * @param token
     * @param usercb
     */
    public void refreshToken(String token, UserCallBack usercb) {
        if (comfunc.checkPara(token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("token", token);
                hsp.doHttpPost(Configuration.REFRESHTOKEN, postParam, usercb);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Check my token.
     *
     * @param token
     * @param usercb
     */
    public void verifyToken(String token, UserCallBack usercb) {
        if (comfunc.checkPara(token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("token", token);
                hsp.doHttpPost(Configuration.VERIFYTOKEN, postParam,
                        usercb);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Forget my password, and reset it.
     *
     * @param password
     * @param token
     * @param usercb
     */
    public void resetPassword(String password, UserCallBack usercb, String token) {
        if (comfunc.checkPara(password, token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("password1", password);
                postParam.put("password2", password);
                hsp.doHttpPost(Configuration.RESETPASSWORD, postParam, usercb, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Update the role of the user.
     *
     * @param deviceid
     * @param enduserid
     * @param usercb
     */
    public void updateBindRole(String deviceid, String enduserid, String role,
                               UserCallBack usercb, String token) {
        if (comfunc.checkPara(deviceid, enduserid, role, token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("deviceid", deviceid);
                postParam.put("enduserid", enduserid);
                postParam.put("role", role);
                hsp.doHttpPut(Configuration.UPDATEBINDROLE, postParam, usercb, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Remove one user.
     *
     * @param deviceid
     * @param enduserid
     * @param usercb
     */
    public void removeBindRole(String deviceid, String enduserid, UserCallBack usercb, String token) {
        if (comfunc.checkPara(deviceid, enduserid, token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("deviceid", deviceid);
                postParam.put("enduserid", enduserid);
                hsp.doHttpPut(Configuration.REMOVEBINDROLE, postParam, usercb, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * Transfer administrator.
     *
     * @param deviceid
     * @param enduserid
     * @param usercb
     */
    public void transferAminUser(String deviceid, String enduserid,
                                 UserCallBack usercb, String token) {
        if (comfunc.checkPara(deviceid, enduserid, token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("deviceid", deviceid);
                postParam.put("enduserid", enduserid);
                hsp.doHttpPut(Configuration.TRANSFERADMIN, postParam, usercb, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * get devicelist
     *
     * @param usercb
     * @param token
     */
    public void getDeviceList(UserCallBack usercb, String token) {
        if (comfunc.checkPara(token)) {
//            String postParam = "";
            String postParam = "?" + Math.random();
            hsp.doHttpGet(Configuration.GETDEVICELIST, postParam, usercb, token);
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * get members of device
     *
     * @param deviceid
     * @param usercb
     * @param token
     */
    public void getMemberList(String deviceid, UserCallBack usercb, String token) {
        if (comfunc.checkPara(deviceid, token)) {
//            String postParam = "?deviceid=" + deviceid;
            String postParam = "?deviceid=" + deviceid + "&" + Math.random();
            hsp.doHttpGet(Configuration.GETMEMBERLIST, postParam, usercb, token);
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * get members of device
     *
     * @param deviceid
     * @param usercb
     * @param token
     */
    public void getDeviceInfo(String deviceid, UserCallBack usercb, String token) {
        if (comfunc.checkPara(deviceid, token)) {
//            String postParam = "?deviceid=" + deviceid;
            String postParam = "?deviceid=" + deviceid + "&" + Math.random();
            hsp.doHttpGet(Configuration.DEVICEINFO, postParam, usercb, token);
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }

    /**
     * get cookbook list by productid and type(such as cake, meat, bread)
     * @param type
     * @param productid
     * @param usercb
     * @param token
     */
    public void getCookBookList(int type, String productid, UserCallBack usercb, String token) {
        if (comfunc.checkPara(productid, token)) {
            String postParam = "?type=" + type + "&productid=" + productid;
            hsp.doHttpGet(Configuration.GETCOOKBOOKLIST, postParam, usercb, token);
        } else {
            comfunc.illegalCallBack(usercb);
        }
    }
}
