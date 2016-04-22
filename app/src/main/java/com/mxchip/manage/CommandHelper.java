package com.mxchip.manage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocke on 2016/04/08.
 */
public class CommandHelper {
    public static String combCommand(CommandBean cb) {

        if (ConstHelper.checkPara(cb.deviceid, cb.appid, cb.enduserid)) {
            String temp = "";
            try {
                JSONObject cmdbody = new JSONObject();
                List<String> list = new ArrayList<String>();

                Class cls = cb.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (Field fl : fields) {

                    switch (fl.getName()) {
                        case "deviceid":
                            cmdbody.put("deviceid", cb.deviceid);
                            break;
                        case "appid":
                            cmdbody.put("appid", cb.appid);
                            break;
                        case "enduserid":
                            cmdbody.put("userid", cb.enduserid);
                            break;
                        case "extrajson":
                            break;
                        //TODO 普通的指令，非食谱(本地或者云食谱)
//                        case "WF":
//                            if(null == cb.extrajson)
//                                break;
//                            list.add("WF");
//                            cmdbody.put("WF", setWF(cb.WF, cb.extrajson));
//                            break;
                        default:
                            try {
                                temp = fl.get(cb).toString();
                                if (ConstHelper.checkPara(temp)) {
                                    list.add(fl.getName());
                                    //TODO 这里是带了value的如:"KG_Start":{"value":"1"}
//                                    cmdbody.put(fl.getName(), setChild(temp));
                                    cmdbody.put(fl.getName(), temp);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                cmdbody.put("atrrSet", new JSONArray(list));
                return cmdbody.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static JSONObject setChild(String value) {
        try {
            JSONObject childbody = new JSONObject();
            childbody.put("value", value);
            return childbody;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject setWF(String value, JSONObject extra) {
        try {
            JSONObject childbody = new JSONObject();
            childbody.put("value", value);
            childbody.put("extra", extra);
            return childbody;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject setExtra(ExtraBean eb) {
        try {
            JSONObject extrabody = new JSONObject();
            List<String> list = new ArrayList<String>();

            Class cls = eb.getClass();
            Field[] fields = cls.getDeclaredFields();
            String temp = "";
            for (Field fl : fields) {
                try {
                    temp = fl.get(eb).toString();
                    if (ConstHelper.checkPara(temp)) {
                        extrabody.put(fl.getName(), temp);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return extrabody;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
