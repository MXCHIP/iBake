package com.mxchip.helper;

/**
 * public static String SENDVERCODE = _URLHEAD + "sendvercode/";
 * public static String CHECKVERCODE = _URLHEAD + "checkvercode/";
 * public static String REGISTER = _URLHEAD + "phonesignup/";
 * public static String LOGININ = _URLHEAD + "token-auth/";
 * public static String REFRESHJWT = _URLHEAD + "token-refresh/";
 * public static String VERIFYJWT = _URLHEAD + "token-verify/";
 * public static String GETSHAREVERCODE = _URLHEAD + "generatebindingvercode/";
 * public static String ADDDEVBYVERCODE = _URLHEAD + "devicegrant/";
 * public static String BINDINGDEV = _URLHEAD + "devicebinding/";
 * public static String UNBINDDEV = _URLHEAD + "bindingselfstop/";
 * public static String SENDCOMMAND = _URLHEAD + "sendcommand/";
 * public static String RESETPASSWORD = _URLHEAD + "accountpwchange/";
 * public static String UPDATEUSERROLE = _URLHEAD + "bindingrole/";
 * public static String REMOVEUSER = _URLHEAD + "bindingremove/";
 * public static String TRANSFERADMINUSER = _URLHEAD + "bindingshift/";
 */
public class Configuration {
    private static String _URLHEAD = "https://iot.mxchip.com/enduser/";
    private static String _COOKBOOK = "https://iot.mxchip.com/cookbook/";


    public static String LOGININ = _URLHEAD + "login/";
    public static String GETVERCODE = _URLHEAD + "getVerCode/";
    public static String CHECKVERCODE = _URLHEAD + "checkVerCode/";
    public static String REFRESHTOKEN = _URLHEAD + "refreshToken/";
    public static String VERIFYTOKEN = _URLHEAD + "verifyToken/";
    public static String GETSHAREVERCODE = _URLHEAD + "getShareVerCode/";
    public static String ADDDEVBYVERCODE = _URLHEAD + "grantDevice/";
    public static String BINDDEVICE = _URLHEAD + "bindDevice/";
    public static String UNBINDDEVICE = _URLHEAD + "unbindDevice/";
    public static String SENDCOMMAND = _URLHEAD + "sendCommand/";
    public static String RESETPASSWORD = _URLHEAD + "resetPassword/";
    public static String UPDATEBINDROLE = _URLHEAD + "updateBindRole/";
    public static String REMOVEBINDROLE = _URLHEAD + "removeBindRole/";
    public static String TRANSFERADMIN = _URLHEAD + "transferAdmin/";
    public static String GETDEVICELIST = _URLHEAD + "devicelistbyenduser/";
    public static String GETMEMBERLIST = _URLHEAD + "enduserlistbydevice/";
    public static String DEVICEINFO = _URLHEAD + "deviceInfo/";
    public static String UPDATEDEVALIAS = _URLHEAD + "updateDeviceAlias/";

    public static String GETCOOKBOOKLIST = _COOKBOOK + "cookbook_list/";

    public static String getTopic(String deviceid) {
        //TODO 使用fog1.0测试
//        return deviceid + "/#";
        return "d2c/" + deviceid + "/status";
    }
}
