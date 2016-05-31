package com.mxchip.activities.ibake;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mico.micosdk.MiCODevice;
import com.mxchip.callbacks.ControlDeviceCallBack;
import com.mxchip.helper.ScheduleTaskParam;
import com.mxchip.manage.ActionSheetDialog;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import java.io.File;

/**
 * Created by Rocke on 2016/03/30.
 */
public class RecipeEditActivity extends AppCompatActivity {
    private static final String TAG = "---Recipeedit---";
    private SetTitleBar stb;

    private LinearLayout recipe_ed_imgly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit);

//        String recipename = (String) getIntent().getSerializableExtra("recipename");
        stb = new SetTitleBar(RecipeEditActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_RECIPEEDIT);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("next", "next");

        initView();
        initOnClick();

        //TODO 测试延时任务
        testTASK();
    }

    private void initView(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recipe_ed_imgly = (LinearLayout)findViewById(R.id.recipe_ed_imgly);

        addMoreKG();
    }

    private void initOnClick(){
        recipe_ed_imgly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheetDialog(RecipeEditActivity.this)
                        .builder()
                        .setTitle("Choose one picture.")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("Camara", ActionSheetDialog.SheetItemColor.Yellow,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.d(TAG, "to ----- Camara");
                                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ConstPara._FILE_ROOT_PATH, "temp.jpg")));
                                        startActivityForResult(intent, ConstPara.CAMERA_REQUEST_CODE);
                                    }
                                })
                        .addSheetItem("Album", ActionSheetDialog.SheetItemColor.Yellow,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.d(TAG, "to ----- Album");
                                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ConstPara.IMAGE_UNSPECIFIED);
                                        startActivityForResult(intent, ConstPara.ALBUM_REQUEST_CODE);
                                    }
                                })
                        .show();
            }
        });
    }

    private void addMoreKG(){

//        LinearLayout recipe_ed_listly = (LinearLayout) findViewById(R.id.recipe_ed_listly);
//        View viewOne1 = getLayoutInflater().inflate(R.layout.recipe_edit_detail_kg, null);
//        View viewOne2 = getLayoutInflater().inflate(R.layout.recipe_edit_detail_kg, null);
//        View viewOne3 = getLayoutInflater().inflate(R.layout.recipe_edit_detail_kg, null);
//        View viewOne4 = getLayoutInflater().inflate(R.layout.recipe_edit_detail_kg, null);
//        recipe_ed_listly.removeAllViews();
//        recipe_ed_listly.addView(viewOne1, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//        recipe_ed_listly.addView(viewOne2, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//        recipe_ed_listly.addView(viewOne3, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//        recipe_ed_listly.addView(viewOne4, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

    }

    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, ConstPara.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 380);
        intent.putExtra("outputY", 612);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ConstPara.CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ConstPara.ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                startCrop(data.getData());
                break;
            case ConstPara.CAMERA_REQUEST_CODE:
                File picture = new File(ConstPara._FILE_ROOT_PATH + "/temp.jpg");
                startCrop(Uri.fromFile(picture));
                break;
            case ConstPara.CROP_REQUEST_CODE:
                if (data == null) {
                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
//                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
//                    display.setImageBitmap(photo); //把图片显示在ImageView控件上
                    recipe_ed_imgly.setBackgroundDrawable(ConstHelper.bitmap2Drawable(photo));
                }
                break;
            default:
                break;
        }
    }

    private MiCODevice micoDev;
    String token;
    private void testTASK(){
        micoDev = new MiCODevice(RecipeEditActivity.this);
        SharePreHelper shareph = new SharePreHelper(RecipeEditActivity.this);
        token = shareph.getData(ConstPara.SHARE_TOKEN);

        Button scheduletask = (Button)findViewById(R.id.scheduletaskid);
        Button delaytask = (Button)findViewById(R.id.delaytaskid);
        Button tasklist = (Button)findViewById(R.id.tasklistid);
        Button removetask = (Button)findViewById(R.id.removetaskid);
        Button stoptask = (Button)findViewById(R.id.stoptaskid);

        /**
         * 创建定时任务
         */
        scheduletask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleTaskParam stp = new ScheduleTaskParam();
                stp.device_id = "d95366fe-06c0-11e6-a739-00163e0204c0";
                stp.commands = "{\"KG_Bottom\":\"1\",\"KG_Start\":\"1\",\"KG_Top\":\"1\",\"Temp_Bottom\":\"240\",\"Temp_Top\":\"240\",\"WorkMode\":\"1\",\"WorkTime\":\"58\",\"appid\":\"d8cdf9c6-de8c-11e5-a739-00163e0204c0\",\"deviceid\":\"d95366fe-06c0-11e6-a739-00163e0204c0\",\"userid\":\"b8b917e2-deaa-11e5-a739-00163e0204c0\",\"atrrSet\":[\"KG_Bottom\",\"KG_Start\",\"KG_Top\",\"Temp_Bottom\",\"Temp_Top\",\"WorkMode\",\"WorkTime\"]}";
                stp.enable = true;

                stp.month = "*";
                stp.day_of_week = "*";
                stp.hour = "*";
                stp.minute = "*";

                micoDev.createScheduleTask(stp, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
            }
        });

        /**
         * 创建延时任务
         */
        delaytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleTaskParam stp = new ScheduleTaskParam();
                stp.device_id = "d95366fe-06c0-11e6-a739-00163e0204c0";
                stp.commands = "{\"KG_Bottom\":\"1\",\"KG_Start\":\"1\",\"KG_Top\":\"1\",\"Temp_Bottom\":\"240\",\"Temp_Top\":\"240\",\"WorkMode\":\"1\",\"WorkTime\":\"58\",\"appid\":\"d8cdf9c6-de8c-11e5-a739-00163e0204c0\",\"deviceid\":\"d95366fe-06c0-11e6-a739-00163e0204c0\",\"userid\":\"b8b917e2-deaa-11e5-a739-00163e0204c0\",\"atrrSet\":[\"KG_Bottom\",\"KG_Start\",\"KG_Top\",\"Temp_Bottom\",\"Temp_Top\",\"WorkMode\",\"WorkTime\"]}";
                stp.enable = true;
                stp.second = 10;

                micoDev.createDelayTask(stp, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
            }
        });

        /**
         * 获取任务列表
         */
        tasklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micoDev.getTaskList("d95366fe-06c0-11e6-a739-00163e0204c0", 1, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
                micoDev.getTaskList("d95366fe-06c0-11e6-a739-00163e0204c0", 0, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
            }
        });

        /**
         * 移除任务
         */
        removetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceid = "d95366fe-06c0-11e6-a739-00163e0204c0";
                String taskid = "ff9c5c3a-2097-11e6-a739-00163e0204c0";
                micoDev.deleteTask(deviceid, taskid, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
            }
        });

        /**
         * 暂停任务
         */
        stoptask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleTaskParam stp = new ScheduleTaskParam();
                stp.device_id = "d95366fe-06c0-11e6-a739-00163e0204c0";
                stp.commands = "{\"KG_Bottom\":\"1\",\"KG_Start\":\"1\",\"KG_Top\":\"1\",\"Temp_Bottom\":\"330\",\"Temp_Top\":\"330\",\"WorkMode\":\"1\",\"WorkTime\":\"58\",\"appid\":\"d8cdf9c6-de8c-11e5-a739-00163e0204c0\",\"deviceid\":\"d95366fe-06c0-11e6-a739-00163e0204c0\",\"userid\":\"b8b917e2-deaa-11e5-a739-00163e0204c0\",\"atrrSet\":[\"KG_Bottom\",\"KG_Start\",\"KG_Top\",\"Temp_Bottom\",\"Temp_Top\",\"WorkMode\",\"WorkTime\"]}";
                stp.task_id = "ff9c5c3a-2097-11e6-a739-00163e0204c0";
                stp.enable = false;

                stp.month = "*";
                stp.day_of_week = "*";
                stp.hour = "*";
                stp.minute = "*";

                micoDev.updateScheduleTask(stp, new ControlDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG + "onSuc", message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG + "onFai", code + " " + message);
                    }
                }, token);
            }
        });
    }
}
