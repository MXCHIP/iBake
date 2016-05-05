package com.mxchip.helper;

/**
 * int mqtttype = 1;
 *
 * String host = "tcp://mqtt.ons.aliyun.com:1883";
 * String userName = "wHQNxxxo9fqHYy";
 * String passWord = "VcVi86xxxATWYmiugoBFh";
 * String clientID = "CID_XXX_TEST@@@M0x";
 *
 * String topic = "CID_XXX_TEST";
 * //如果该设备需要接收点对点的推送，那么需要订阅二级topic，topic/p2p/，但凡以topic/p2p/为前缀的，都认为是点对点推送
 * String p2ptopic = topic + "/p2p/";
 * //同时订阅两个topic，一个是基于标准mqtt协议的发布订阅模式，一个是扩展的点对点推送模式
 *
 * String[] topicFilters = new String[]{topic, p2ptopic};
 */
public class ListenDeviceParams {
    public String deviceid;
    public String host;
    public String port;
    public String userName;
    public String passWord;
    public String clientID;
    /**
     * 0 fogcloud2.0
     * 1 AliMQTT
     */
    public int mqtttype;
    public String[] topicFilters;

    public ListenDeviceParams() {
        this.deviceid = null;
        this.host = null;
        this.port = null;
        this.userName = null;
        this.passWord = null;
        this.clientID = null;
        this.mqtttype = 0;
        this.topicFilters = null;
    }
}
