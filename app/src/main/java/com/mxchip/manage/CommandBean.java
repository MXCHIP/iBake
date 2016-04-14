package com.mxchip.manage;

import org.json.JSONObject;

/**
 * Created by Rocke on 2016/04/08.
 */
public class CommandBean {
    public String deviceid = "";
    public String appid = "";
    public String enduserid = "";
    public String KG_Start = "";
    public String KG_Pause = "";
    public String KG_Turn = "";
    public String KG_Preheat = "";
    public String KG_Light = "";
    public String KG_Fan = "";
    public String WF = "";
    public JSONObject extrajson = null;

    public CommandBean() {
        this.deviceid = "";
        this.appid = "";
        this.enduserid = "";
        this.KG_Start = "";
        this.KG_Pause = "";
        this.KG_Turn = "";
        this.KG_Preheat = "";
        this.KG_Light = "";
        this.KG_Fan = "";
        this.WF = "";
        this.extrajson = null;
    }
}
