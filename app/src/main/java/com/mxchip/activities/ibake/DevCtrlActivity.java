package com.mxchip.activities.ibake;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mico.micosdk.MiCODevice;
import com.mxchip.callbacks.ControlDeviceCallBack;
import com.mxchip.helper.ListenDeviceParams;
import com.mxchip.helpers.CircleProgressBar;
import com.mxchip.helpers.GetDelicacy;
import com.mxchip.helpers.TextMoveLayout;
import com.mxchip.helpers.WheelView;
import com.mxchip.manage.CommandBean;
import com.mxchip.manage.CommandHelper;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;
import com.mxchip.manage.TempMap;
import com.mxchip.manage.WorkStatusMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Rocke on 2016/03/07.
 */
public class DevCtrlActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "---DevCtrlActivity---";
    private SetTitleBar stb;
    private Context context;
    private MiCODevice micoDev;

    /**
     * 进度标志点
     */
    private ImageView circlePointImg;
    private ImageView dev_ctrl_btnsid;
    private ImageView dev_ctrl_light;
    private LinearLayout frameSwitch;
    private NavigationView navigationView;
    private LinearLayout devctrl_stop_ly;
    private LinearLayout devctrl_start_ly;
    private LinearLayout devctrl_ok_ly;

    private Button devctrl_stop_pausebtn;
    private Button devctrl_stop_stopbtn;
    private Button devctrl_start_startid;
    private Button devctrl_ok_cancelbtn;
    private Button devctrl_ok_okbtn;

    private View view_ctrl_swi;
    private View view_swi_time;
    private TextView temptop_txt;
    private TextView tempbottom_txt;
    private TextView baketime_txt;
    private TextView startTime, endTime;
    private SeekBar seekbar = null;

    private TextView circletitle_txt;

    /**
     * 进度条
     */
    private CircleProgressBar mBar;
    private SharePreHelper shareph;

    private String deviceid;
    private String devicename;
    private String devicepw;
    private String token;
    private String enduserid;

    private CommandBean cb = null;
    private int tempTAG = 0;//1 上管 2 下管 3 烘烤时间
    private String chooseTemp;//1 上管 2 下管
    private int chooseTime = 30;//烘烤时间
    private int chooseTimeTmp = 0;//烘烤时间

    float moveStep = 0; //托动条的移动步调
    TextView text;
    int screenWidth; //屏幕宽度
    String startTimeStr = ConstPara.START_TIME;
    String endTimeStr = ConstPara.END_TIME;
    int st_h,st_m;

    //workstatus标记 false界面参数不可调，true界面参数可调
    Boolean WSTAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dev_ctrl_homepage);

        context = DevCtrlActivity.this;
        micoDev = new MiCODevice(context);
        shareph = new SharePreHelper(context);

        devicename = (String) getIntent().getSerializableExtra(ConstPara.INTENT_DEVNAME);
        stb = new SetTitleBar(DevCtrlActivity.this);
        stb.setTitleName(devicename);
        stb.setLeftButton("drawer", "drawer");
        stb.setRightButton("cloud", "cloud");

        initView();
        initOnClick();
        initCommandPara();
        initThisPage();

        getDeviceInfo();
    }

    private void initView() {
        frameSwitch = (LinearLayout) findViewById(R.id.frameSwitch);
        dev_ctrl_btnsid = (ImageView) findViewById(R.id.dev_ctrl_btnsid);
        circlePointImg = (ImageView) findViewById(R.id.circle_point_img);
        dev_ctrl_light = (ImageView) findViewById(R.id.dev_ctrl_lightid);
        mBar = (CircleProgressBar) findViewById(R.id.myProgress);
        navigationView = (NavigationView) findViewById(R.id.dev_ctrl_navview);

        devctrl_stop_ly = (LinearLayout) findViewById(R.id.devctrl_stop_lyid);
        devctrl_start_ly = (LinearLayout) findViewById(R.id.devctrl_start_lyid);
        devctrl_ok_ly = (LinearLayout) findViewById(R.id.devctrl_ok_lyid);

        devctrl_stop_pausebtn = (Button) findViewById(R.id.devctrl_stop_pausebtn);
        devctrl_stop_stopbtn = (Button) findViewById(R.id.devctrl_stop_stopbtn);
        devctrl_start_startid = (Button) findViewById(R.id.devctrl_start_startid);
        devctrl_ok_cancelbtn = (Button) findViewById(R.id.devctrl_ok_cancelbtn);
        devctrl_ok_okbtn = (Button) findViewById(R.id.devctrl_ok_okbtn);
        circletitle_txt = (TextView) findViewById(R.id.devctrl_circletitle_tvid);

        //控制菜单view
        view_ctrl_swi = getLayoutInflater().inflate(R.layout.dev_ctrl_swi_btns, null);
        view_swi_time = getLayoutInflater().inflate(R.layout.dev_ctrl_swi_time, null);
        //上管txt
        temptop_txt = (TextView)view_ctrl_swi.findViewById(R.id.devctrl_uptemp_tvid);
        //下管txt
        tempbottom_txt = (TextView)view_ctrl_swi.findViewById(R.id.devctrl_downtemp_tvid);
        //工作时间txt
        baketime_txt = (TextView)view_ctrl_swi.findViewById(R.id.devctrl_baketime_tvid);

        //初始化seekbar

        text = new TextView(this);
        text.setBackgroundColor(Color.rgb(255, 255, 255));
        text.setTextColor(Color.rgb(252, 203, 15));
        text.setTextSize(15);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(screenWidth, 50);
        //自定义随着拖动条一起移动的空间
        TextMoveLayout textMoveLayout = (TextMoveLayout) view_swi_time.findViewById(R.id.textLayout);
        textMoveLayout.addView(text, layoutParams);
        /**
         * findView
         */
        seekbar = (SeekBar) view_swi_time.findViewById(R.id.seekbar);
        startTime = (TextView) view_swi_time.findViewById(R.id.start_time);
        endTime = (TextView) view_swi_time.findViewById(R.id.end_time);
        /**
         * setListener
         */
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
    }

    /**
     * 初始化些必备参数
     */
    private void initCommandPara() {

        deviceid = (String) getIntent().getSerializableExtra(ConstPara.INTENT_DEVID);
        devicepw = (String) getIntent().getSerializableExtra(ConstPara.INTENT_DEVPW);
        enduserid = shareph.getData(ConstPara.SHARE_ENDERUSERID);
        token = shareph.getData(ConstPara.SHARE_TOKEN);
        reSetCommand();

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    private void reSetCommand() {
        cb = new CommandBean();

        cb.appid = ConstPara._APPID;
        cb.deviceid = deviceid;
        cb.enduserid = enduserid;
    }

    private void initOnClick() {
        navigationView.setNavigationItemSelectedListener(this);
        stb.initNavHeaderLayOnClick(navigationView);

        dev_ctrl_btnsid.setOnClickListener(new View.OnClickListener() {
//            boolean isScreen = false;

            @Override
            public void onClick(View v) {

                if (v.getTag() == "start") {
                    v.setTag("stop");
                    getSimpListView();
                    v.setBackgroundResource(R.drawable.baking_btn_setting_off);
                } else {
                    v.setTag("start");
                    getViewTwo();
                    v.setBackgroundResource(R.drawable.baking_btn_setting_on);
                }
            }
        });

        /**
         * 控制灯的开关
         */
        dev_ctrl_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getTag() == "on") {
                    showKGLight("off", v);

                    cb.KG_Light = "0";
                    sendCommand(CommandHelper.combCommand(cb));
                } else {
                    showKGLight("on", v);

                    cb.KG_Light = "1";
                    sendCommand(CommandHelper.combCommand(cb));
                }
            }
        });

        // 点击cancel按钮
        devctrl_ok_cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewTwo();
                showdevCtrlButton("start");
            }
        });

        // 点击OK按钮
        devctrl_ok_okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewTwo();
                showdevCtrlButton("start");
                switch (tempTAG) {
                    case 1:
                        if (ConstHelper.checkPara(chooseTemp)) {
                            cb.KG_Top = "1";
                            cb.Temp_Top = chooseTemp;
                            temptop_txt.setText(chooseTemp + ConstPara.DEGREE);
                        }
                        break;
                    case 2:
                        if (ConstHelper.checkPara(chooseTemp)) {
                            cb.KG_Bottom = "1";
                            cb.Temp_Bottom = chooseTemp;
                            tempbottom_txt.setText(chooseTemp + ConstPara.DEGREE);
                        }
                        break;
                    case 3:
                        if(0 !=chooseTimeTmp){
                            chooseTime = chooseTimeTmp;
                            cb.WorkTime = chooseTime + "";
                            baketime_txt.setText(chooseTime + ConstPara.MINUTES);
                        }
                        break;
                    default:
                        break;
                }
                chooseTemp = null;
                chooseTimeTmp = 0;
            }
        });

        devctrl_start_startid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showdevCtrlButton("stop");

                cb.KG_Start = "1";
                cb.WorkMode = "1";
//                cb.WF = "0";

//                cb.KG_Turn = "1";
//                cb.KG_Fan = "0";

//                cb.extrajson = CommandHelper.setExtra(eb);
                sendCommand(CommandHelper.combCommand(cb));
            }
        });

        devctrl_stop_stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdevCtrlButton("start");
                cb.KG_Start = "0";
                sendCommand(CommandHelper.combCommand(cb));
            }
        });
        devctrl_stop_pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("PAUSE".equals(devctrl_stop_pausebtn.getText().toString().trim())){
                    showdevCtrlButton("resume");
                    cb.KG_Pause = "1";
                    sendCommand(CommandHelper.combCommand(cb));
                }else{
                    showdevCtrlButton("pause");
                    cb.KG_Pause = "0";
                    sendCommand(CommandHelper.combCommand(cb));
                }
            }
        });
    }

    private void showKGLight(String type, View v) {
        switch (type) {
            case "off":
                v.setTag("off");
                v.setBackgroundResource(R.drawable.baking_btn_light_off);
                break;
            case "on":
                v.setTag("on");
                v.setBackgroundResource(R.drawable.baking_btn_light_on);
                break;
        }
    }


   private void showdevCtrlButton(String type) {
        switch (type) {
            case "start":
                WSTAG = true;
                devctrl_start_ly.setVisibility(View.VISIBLE);
                devctrl_stop_ly.setVisibility(View.GONE);
                devctrl_ok_ly.setVisibility(View.GONE);
                break;
            case "stop":
                WSTAG = false;
                devctrl_stop_ly.setVisibility(View.VISIBLE);
                devctrl_start_ly.setVisibility(View.GONE);
                devctrl_ok_ly.setVisibility(View.GONE);
                break;
            case "ok":
                devctrl_ok_ly.setVisibility(View.VISIBLE);
                devctrl_start_ly.setVisibility(View.GONE);
                devctrl_stop_ly.setVisibility(View.GONE);
                break;
            case "resume":
                devctrl_stop_pausebtn.setText("RESUME");
                break;
            case "pause":
                devctrl_stop_pausebtn.setText("PAUSE");
                break;
        }
    }

    private void initThisPage() {

        getSimpListView();
//        getDeviceInfo();

        dev_ctrl_btnsid.setTag("close");
//        showKGLight("on", dev_ctrl_light);
    }

    /**
     * 推荐食谱页面
     */
    public void getSimpListView() {
        View simp_list = getLayoutInflater().inflate(R.layout.sweet_time, null);
        frameSwitch.removeAllViews();
        frameSwitch.addView(simp_list, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        initDelicacy();
    }

    /**
     * 加载页面数据
     */
    private void initDelicacy() {
        GetDelicacy gdc = new GetDelicacy(DevCtrlActivity.this);
        gdc.refreshDelicy();
    }

    /**
     * 控制BUTTON页面
     */
    public void getViewTwo() {

        frameSwitch.removeAllViews();
        frameSwitch.addView(view_ctrl_swi, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        // 上管温度
        LinearLayout uptemp_lay_id = (LinearLayout) findViewById(R.id.uptemp_lay_id);
        uptemp_lay_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WSTAG){
                    tempTAG = 1;
                    getTempView(temptop_txt.getText().toString().trim());
                    showdevCtrlButton("ok");
                }
            }
        });
        // 下管温度
        LinearLayout downtemp_lay_id = (LinearLayout) findViewById(R.id.downtemp_lay_id);
        downtemp_lay_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WSTAG) {
                    tempTAG = 2;
                    getTempView(tempbottom_txt.getText().toString().trim());
                    showdevCtrlButton("ok");
                }
            }
        });
        //烘烤时间
        LinearLayout baketime_lay_id = (LinearLayout) findViewById(R.id.baketime_lay_id);
        baketime_lay_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WSTAG) {
                    tempTAG = 3;
                    getBakeView();
                    showdevCtrlButton("ok");
                }
            }
        });
        //预热
        LinearLayout preheat_lay_id = (LinearLayout) findViewById(R.id.preheat_lay_id);
        preheat_lay_id.setTag("close");
        preheat_lay_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WSTAG) {
                    if (v.getTag() == "open") {
                        v.setTag("close");
                        v.setBackgroundResource(R.drawable.full_box);
                        cb.KG_Preheat = "0";
                    } else {
                        v.setTag("open");
                        v.setBackgroundColor(Color.parseColor("#FCCB0F"));
                        cb.KG_Preheat = "1";
                    }
                }
            }
        });
    }

    /**
     * 烘烤时间
     */
    public void getBakeView() {
        frameSwitch.removeAllViews();
        frameSwitch.addView(initTimeChoose(), LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    }
    /**
     * 温度选择框
     */
    public void getTempView(String tempVal) {
        View tempview = getLayoutInflater().inflate(R.layout.dev_ctrl_swi_temp, null);

        WheelView wva = (WheelView) tempview.findViewById(R.id.main_wv);

        wva.setOffset(1);
        wva.setItems(Arrays.asList(ConstPara.PLANETS));
        wva.setSeletion(TempMap.getIndexFromTempMap(tempVal, Arrays.asList(ConstPara.PLANETS)));
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                chooseTemp = item.substring(0, item.indexOf(ConstPara.DEGREE));
            }
        });

        frameSwitch.removeAllViews();
        frameSwitch.addView(tempview, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        stb.commonOnNavItemSelected(item);
        return true;
    }

    //获取设备的状态信息
    private void getDeviceInfo() {

        ListenDeviceParams ldp = new ListenDeviceParams();
        ldp.host = ConstPara.MQTT_HOST;
        ldp.port = ConstPara.MQTT_PORT;
        ldp.userName = enduserid;
        ldp.passWord = shareph.getData(ConstPara.SHARE_MQTTPW);
        ldp.deviceid = deviceid;
        ldp.clientID = enduserid;

//        ldp.deviceid = deviceid;
//        ldp.mqtttype = 1;
//        ldp.host = "tcp://mqtt.ons.aliyun.com:1883";
//        ldp.userName = "wHQNXGLIEo9fqHYy";
//        ldp.passWord = "VcVi86gK3JVefO8Yo3ATWYmiugoBFh";
//        ldp.clientID = "CID_MXCHIP_TEST@@@M0025";
//        String topic = "MXCHIP_TEST";
//        String p2ptopic = topic + "/p2p/";
//        ldp.topicFilters = new String[]{topic, p2ptopic};

        /**
         * TODO 使用fog1.0测试
         */

        micoDev.startListenDevice(ldp, new ControlDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "ListenonSuccess", message);

                shareph.addData(ConstPara.SHARE_LASTDEVNAME, devicename);
                shareph.addData(ConstPara.SHARE_LASTDEVICEID, deviceid);
                shareph.addData(ConstPara.SHARE_LASTDEVICEPW, devicepw);

                cb.GET_STATUS = "0";
                sendCommand(CommandHelper.combCommand(cb));
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + "onFailure", code + " " + message);
            }

            @Override
            public void onDeviceStatusReceived(String msgType, String messages) {
                Log.d(TAG + "onDeviceStatusReceived", msgType + " " + messages);
                dealWithStatus(messages);
            }
        });
    }

    //暂未用到
    Handler dealhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "1");
            }
        }
    };

    //只是为了效果显示
//    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            Log.d(TAG + "thread", "start");
//            while (tag >= 0) {
//                try {
//                    mBar.setProgressNotInUiThread(tag--, circlePointImg);
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            Log.d(TAG + "thread", "finish");
//        }
//    });


    /**
     * 这里处理设备上报的数据
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (1 == msg.what) {
                refreshPage(msg.obj);
            }
            return true;
        }
    });

    //    处理获得的指令
    private void dealWithStatus(String payload) {
        if(payload.indexOf("{") > -1){
            JSONObject tttjson = ConstHelper.getPayload(payload);
            if(null != tttjson){
                Message msg = new Message();
                msg.what = 1;
                msg.obj = tttjson;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 刷新界面的数据
     * KG_Power
     * KG_Light
     * WorkTime
     * WorkStatus
     * EC
     * WF
     * KG_Preheat
     * Temp_Top
     * Temp_Bottom
     * WF_CurrentStep
     * WF_TimeLeft
     * Cur_TempBottom
     * Cur_TempTop
     *
     * @param obj
     */
    private void refreshPage(Object obj){

        try {
            JSONObject jsonTmp = new JSONObject(obj.toString());
            JSONArray jsarr = jsonTmp.getJSONArray("attrSet");

            if(null == jsarr)
                return;

            if(WSTAG)
                return;

            String jsonKey = "";
            String jsonVal = "";
            for(int i = 0; i < jsarr.length(); i++){
                jsonKey = jsarr.getString(i);

//                jsonVal =  ConstHelper.getJsonValue(jsonTmp.getString(jsonKey));
                jsonVal =  jsonTmp.getString(jsonKey);
                switch (jsonKey){
//                    case "KG_Start":
//                        if("0".equals(jsonVal)){
//                            showdevCtrlButton("start");
//
//                        }else if("1".equals(jsonVal))
//                            showdevCtrlButton("stop");
//                        break;
                    case "KG_Light":
                        if("1".equals(jsonVal))
                            showKGLight("on", dev_ctrl_light);
                        else
                            showKGLight("off", dev_ctrl_light);
                        break;
                    case "WorkTime":
                        baketime_txt.setText(jsonVal + ConstPara.MINUTES);
                        break;
                    case "KG_Preheat":
//                        circletitle_txt.setText(jsonVal);
//                        showdevCtrlButton("stop");
                        break;
                    case "WorkStatus":
                        updateCircleTitle(jsonVal);
                        break;
                    case "Temp_Top":
                        temptop_txt.setText(jsonVal + ConstPara.DEGREE);
                        break;
                    case "Temp_Bottom":
                        tempbottom_txt.setText(jsonVal + ConstPara.DEGREE);
                        break;
                    case "WF_TimeLeft":
                        mBar.setProgressNotInUiThread(Integer.parseInt(jsonVal), circlePointImg);
                        break;
                }
//                ConstHelper.getJsonValue(tttjson.getString("WF_TimeLeft"))
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO 这里是动态修改剩余时间的圆环
//        int nowti = Integer.parseInt(obj.toString()) - (hhh++);
//        if(nowti > 0){
//            mBar.setProgressNotInUiThread(nowti, circlePointImg);
//        }
    }
    WorkStatusMap wsm = new WorkStatusMap();
    private void updateCircleTitle(String status){

        circletitle_txt.setText(wsm.getStatusName(status));

        switch (status){
            case WorkStatusMap.DJ_CODE:
                showdevCtrlButton("start");
                break;
            case WorkStatusMap.YY_CODE:
                showdevCtrlButton("stop");
                break;
            case WorkStatusMap.HK_CODE:
                showdevCtrlButton("stop");
                break;
            case WorkStatusMap.ZT_CODE:
                showdevCtrlButton("resume");
                break;
            case WorkStatusMap.YR_CODE:
                showdevCtrlButton("stop");
                break;
            case WorkStatusMap.WC_CODE:
                showdevCtrlButton("start");
                break;
            case WorkStatusMap.YRWC_CODE:
                showdevCtrlButton("start");
                break;
        }
//        if(wsm.DJ_CODE.equals(status)){
//            showdevCtrlButton("start");
//        }else{
//            showdevCtrlButton("stop");
//        }
    }

    //发送指令
    private void sendCommand(String command) {

        String commandType = ConstPara.MQTT_CMD_TYPE;

        String token = shareph.getData(ConstPara.SHARE_TOKEN);
        micoDev.sendCommand(deviceid, devicepw, command, commandType, new ControlDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "onSuccess", message);
                reSetCommand();
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + "onFailure", code + " " + message);
            }

        }, token);
    }

    private View initTimeChoose(){
        initProgressTime();

        text.layout((int) (chooseTime * moveStep), 20, screenWidth, 80);

        startTime.setText(startTimeStr);
        endTime.setText(endTimeStr);
        text.setText(getCheckTimeBySeconds(chooseTime));

        //视频组中第一个和最后一个视频之间的总时长
        int totalSeconds = totalSeconds(startTimeStr, endTimeStr);

        seekbar.setEnabled(true);
        seekbar.setMax(totalSeconds);
        seekbar.setProgress(chooseTime);
        moveStep = (float) (((float) screenWidth / (float) totalSeconds) * 0.8);

        return view_swi_time;
    }

    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {

        // 触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            chooseTimeTmp = progress;

            text.layout((int) (progress * moveStep), 20, screenWidth, 80);
            text.setText(getCheckTimeBySeconds(progress));
        }

        // 表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        // 停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    }

    /**
     * 计算连个时间之间的秒数
     */
    private int totalSeconds(String startTime, String endTime) {

        String[] et = endTime.split(":");

        int et_h = Integer.valueOf(et[0]);
        int et_m = Integer.valueOf(et[1]);

        int totalSeconds = (et_h - st_h) * 60 + (et_m - st_m);
        return totalSeconds;
    }

    /**
     * 根据当前选择的秒数还原时间点
     */
    private String getCheckTimeBySeconds(int progress) {

        String return_h = "", return_m = "";

        //得出时
        int h = progress / 60;
        //得出分
        int m = progress % 60;
        if ((m + st_m) >= 60) {
            int tmpMin = (m + st_m) % 60;
            h = h + 1;
            if (tmpMin >= 10) {
                return_m = tmpMin + "";
            } else {
                return_m = "0" + (tmpMin);
            }
        } else {
            if ((m + st_m) >= 10) {
                return_m = (m + st_m) + "";
            } else {
                return_m = "0" + (m + st_m);
            }
        }
        if ((st_h + h) < 10) {
            return_h = "0" + (st_h + h);
        } else {
            return_h = st_h + h + "";
        }
        String nowTime = return_h + ":" + return_m;

        return nowTime;
    }

    private void initProgressTime(){
        String[] st = startTimeStr.split(":");

        st_h = Integer.valueOf(st[0]);
        st_m = Integer.valueOf(st[1]);

        chooseTimeTmp = 0;
    }

    /**
     * 关闭此页面时候关闭MQTT连接
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        micoDev.stopListenDevice(new ControlDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "onDestroy onSuccess", message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + "onDestroy onFailure", code + " " + message);
            }
        });
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ConstHelper.setToast(this, ConstPara._AGAINFINISH);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
