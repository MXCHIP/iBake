package com.mxchip.helper;

/**
 * Created by Rocke on 2016/05/03.
 */
public class ScheduleTaskParam {

    //公共
//    public int task_type; //0定时任务 1延时任务
    public String task_name; //一个uuid，创建task时会生成，只在取指定task时，传入，获取用户全部task时，不需要传入
    public String device_id; //设备ID
    public String order; //指令
    public boolean enable; // 当前task，True 启用 False 暂停

    //定时任务
    public String month; //月 可以为*
    public String day_of_month; //日 可以为*
    /**
     * 星期，取值：
     * 周一：0
     * 周二：1
     * 周三：2
     * 周四：3
     * 周五：4
     * 周六：5
     * 周日：6
     * "*"：每天
     * 不传：单次任务
     * （例如“0,1,2”表示周一 周二 周三）
     */
    public String day_of_week;
    public String hour;
    public String minute;

    //延时任务
    public int second; //延时秒数

    public ScheduleTaskParam(){
//        this.task_type = 0;
        this.task_name = "";
        this.device_id = "";
        this.order = "";
        this.enable = true;

        this.month = "";
        this.day_of_month = "";
        this.day_of_week = "";
        this.hour = "";
        this.minute = "";

        this.second = 0;
    }
}
