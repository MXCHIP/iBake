package com.mxchip.manage;

import android.graphics.Color;

/**
 * Created by Rocke on 2016/03/09.
 */
public class ConstPara {

    //    ERROR code
    public static String _SUCCESSCODE = "0";

    //APP在云端注册后得到的APPID的值
    public static String _APPID = "d8cdf9c6-de8c-11e5-a739-00163e0204c0";
    public static String _PRODUCTID = "6486b2d1-0ee9-4647-baa3-78b9cbc778f7";
	public static String _DEVMAC = "d0bae40c2fee";
    //device ID
    public static String DEVICEID = "05be3a4e-ea5a-11e5-a739-00163e0204c0";
    //device password
    public static String DEVICEPW = "9605";
    public static String MDNS_SER_NAME = "_easylink._tcp.local.";

    //分享用户的权限 2：管理员 3：普通用户
    public static int ROLE = 2;
    //绑定类型
    public static String BINDINGTYPE = "sa";
    //注册和删除activity的名字
    public static String INDEX_PAGE = "ibake_index";

    public static String HOME_PAGE = "homepage";
    public static String RSPSW_PAGE = "reset_password";
    public static String RSPSW_CKCODE_PAGE = "reset_psw_checkcode";
    public static String SSID_PAGE = "add_dev_ssid";
    public static String INTENT_TYPE_RG = "register";
    public static String INTENT_TYPE_RSPSW = "resetpassword";
    public static String SSID_NAME_BODY = "NO WiFi";

    public static String PARA_EMPTY = "Parameters can not be empty.";
    public static String IS_OFFLINE = "This device is offline.";
    public static int IS_OFFLINE_COLOR = Color.rgb(173, 173, 173);


    public static String ISWORKING = "is working";
    public static String MQTT_HOST = "115.28.161.90";

    public static String MQTT_PORT = "1883";
    public static String MQTT_CMD_TYPE = "json";
    public static String TITLENAME_RECIPE = "RECIPE";

    public static String TITLENAME_RESETPASSWORD = "RESET PASSWORD";
    public static String TITLENAME_MYDEVICE = "MY DEVICE";
    public static String TITLENAME_OFFICAL = "OFFICAL";

    public static String TITLENAME_OTHER = "OTHER";
    public static String TITLENAME_BREAD = "BREAD";
    public static String TITLENAME_CAKE = "CAKE";
    public static String TITLENAME_BISCUIT = "BISCUIT";
    public static String TITLENAME_PIZZA = "PIZZA";
    public static String TITLENAME_FISH = "FISH";
    public static String TITLENAME_MEAT = "MEAT";
    public static String TITLENAME_CHICKEN = "CHICKEN";
    public static String TITLENAME_EGGTAN = "EGG TANT";
    public static String TITLENAME_MUFFIN = "MUFFIN";
    public static String TITLENAME_RECIPEEDIT = "RECIPE EDIT";
    public static String TITLENAME_STEP = "STEP 1";

    public static String SHARE_GUIDE = "guide";
    public static String SHARE_USERNAME = "username";
    public static String SHARE_ENDERUSERID = "enduserid";
    public static String SHARE_MQTTPW = "mqttpw";
    public static String SHARE_TOKEN = "token";
    public static String SHARE_LASTDEVNAME = "lastdevicename";
    public static String SHARE_LASTDEVICEID = "lastdeviceid";
    public static String SHARE_LASTDEVICEPW = "lastdevicepw";

    public static String INTENT_DEVNAME = "devicename";
    public static String INTENT_DEVID = "deviceid";
    public static String INTENT_DEVPW = "devicepw";

    public static final int ALBUM_REQUEST_CODE = 1;

    public static final int CAMERA_REQUEST_CODE = 2;
    public static final int CROP_REQUEST_CODE = 4;
    public static String IMAGE_UNSPECIFIED = "image/*";
//    以下是测试数据

    public static String tempJson = "{\"topic\":\"d2c/e79f0250-ea5e-11e5-a739-00163e0204c0/status\",\"payload\":{'deviceid':'e79f0250-ea5e-11e5-a739-00163e0204c0','attrSet':['KG_Power','KG_Fan','KG_Turn','KG_Light','WorkTime','WorkStatus','EC','WF','KG_Preheat','Temp_Top','Temp_Bottom','WF_CurrentStep','WF_TimeLeft','Cur_TempBottom','Cur_TempTop'],'KG_Power':{'value':'0'},'KG_Fan':{'value':'0'},'KG_Turn':{'value':'0'},'KG_Light':{'value':'1'},'WorkTime':{'value':'20'},'WorkStatus':{'value':'0'},'EC':{'value':'0'},'WF':{'value':'1','extra':{'WorkMode':'0','WF_ID':'0','Type':'0','StepNum':'0','TimeTotal':'0'}},'KG_Preheat':{'value':'0'},'Temp_Top':{'value':'35'},'Temp_Bottom':{'value':'65'},'WF_CurrentStep':{'value':'0'},'WF_TimeLeft':{'value':'46'},'Cur_TempBottom':{'value':'36'},'Cur_TempTop':{'value':'80'}}}";

    public static String[] PLANETS = new String[]{"250℃","245℃","240℃","235℃","230℃","225℃","220℃","215℃","210℃","205℃","200℃","195℃","190℃","185℃","180℃","175℃","170℃","165℃","160℃","155℃","150℃","145℃","140℃","135℃","130℃","125℃","120℃","115℃","110℃","105℃","100℃","95℃","90℃","85℃","80℃","75℃","70℃","65℃","60℃"};
    public static String DEGREE = "℃";
    public static String MINUTES = "min";
    public static String START_TIME = "00:00";
    public static String END_TIME = "03:00";

}

