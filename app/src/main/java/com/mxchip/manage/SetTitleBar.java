package com.mxchip.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dtr.zxing.bean.ComminuteCode;
import com.mxchip.activities.ibake.CloudCookBookActivity;
import com.mxchip.activities.ibake.HomePageActivity;
import com.mxchip.activities.ibake.IBakeLoginActivity;
import com.mxchip.activities.ibake.MyDeviceActivity;
import com.mxchip.activities.ibake.R;
import com.mxchip.activities.ibake.RecipeEditActivity;
import com.mxchip.activities.ibake.RecipeEditStepActivity;
import com.mxchip.activities.qrcodeactivity.MiCOQrCodeActivity;

/**
 * Created by Rocke on 2016/03/09.
 */
public class SetTitleBar {

    private String TAG = "---SetTitleBar---";

    private Activity baractivity;

    private TextView toolbar_title;
    private ImageView toolbar_left_img;
    private ImageView toolbar_right_img;
    private DrawerLayout drawer;
    private LinearLayout nav_header_lay;

    public SetTitleBar(Activity activity) {
        this.baractivity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        toolbar_title = (TextView) activity.findViewById(R.id.toolbar_title);
        toolbar_left_img = (ImageView) activity.findViewById(R.id.toolbar_left_img);
        toolbar_right_img = (ImageView) activity.findViewById(R.id.toolbar_right_img);
    }

    public void setLeftButton(String buttonImg, final String func) {

        switch (buttonImg) {
            case "back":
                toolbar_left_img.setImageResource(R.drawable.baking_return);
                break;
            case "none":
                toolbar_left_img.setVisibility(View.INVISIBLE);
                break;
            case "drawer":
                drawer = (DrawerLayout) baractivity.findViewById(R.id.drawer_layout);
                toolbar_left_img.setImageResource(R.drawable.baking_menu);
                break;
        }

        toolbar_left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (func) {
                    case "finish":
                        baractivity.finish();
                        break;
                    case "drawer":
                        drawer.openDrawer(GravityCompat.START);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void initNavHeaderLayOnClick(NavigationView navigationView) {
        nav_header_lay = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_header_layid);
        nav_header_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "---nav_header_lay");
                SharePreHelper shareph = new SharePreHelper(baractivity);
                shareph.removeData("token");
                Intent intent = new Intent(baractivity, IBakeLoginActivity.class);
                baractivity.startActivity(intent);
                baractivity.finish();
            }
        });
    }

    public void setTitleName(String name) {
        toolbar_title.setText(name);
    }

    public void setRightButton(String buttonImg, final String func) {
        switch (buttonImg) {
            case "scan":
                toolbar_right_img.setImageResource(R.drawable.mydevice_scan);
                break;
            case "cloud":
                toolbar_right_img.setImageResource(R.drawable.baking_recipe);
                break;
            case "delete":
                toolbar_right_img.setImageResource(R.drawable.mydevice_icon_delete);
                break;
            case "skip":
                toolbar_right_img.setImageResource(R.drawable.right_close);
                break;
            case "none":
                toolbar_right_img.setVisibility(View.INVISIBLE);
                break;
            case "edit":
                toolbar_right_img.setImageResource(R.drawable.recipe_icon_edit);
                break;
            case "next":
                toolbar_right_img.setImageResource(R.drawable.recipe_edit_next);
                break;
            case "save":
                toolbar_right_img.setImageResource(R.drawable.login_agree);
                break;
        }

        if (!"".equals(func)) {
            toolbar_right_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (func) {
                        case "finish":
                            Intent intent = new Intent(baractivity, HomePageActivity.class);
                            baractivity.startActivity(intent);
                            baractivity.finish();
                            ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
                            ama.destoryActivity("ibake_index");
                            baractivity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            break;
                        case "cloud":
                            Intent cloudintent = new Intent(baractivity, CloudCookBookActivity.class);
                            baractivity.startActivity(cloudintent);
                            baractivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "scan":
                            Log.d(TAG, "---scan");
                            Intent intentscan = new Intent(baractivity, MiCOQrCodeActivity.class);
                            baractivity.startActivityForResult(intentscan, ComminuteCode.RESULT_CODE);
                            break;
                        case "edit":
                            Log.d(TAG, "---edit");
                            Intent intentedit = new Intent(baractivity, RecipeEditActivity.class);
                            baractivity.startActivity(intentedit);
                            baractivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "next":
                            Log.d(TAG, "---next");
                            Intent intentnext = new Intent(baractivity, RecipeEditStepActivity.class);
                            baractivity.startActivity(intentnext);
                            baractivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "save":
                            Log.d(TAG, "---save");
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }


    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }


    public void commonOnNavItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_message) {
            // Handle the camera action
            Log.d(TAG, "---nav_message");
        } else if (id == R.id.nav_recipe) {
            Log.d(TAG, "---nav_recipe");

        } else if (id == R.id.nav_device) {
            Log.d(TAG, "---nav_device");
            intent = new Intent(baractivity, MyDeviceActivity.class);

        } else if (id == R.id.nav_store) {
            Log.d(TAG, "---nav_store");

        } else if (id == R.id.nav_feedback) {
            Log.d(TAG, "---nav_feedback");

        } else if (id == R.id.nav_about) {
            Log.d(TAG, "---nav_about");
        }

        closeDrawer();
        if (null != intent) {
            baractivity.startActivity(intent);
            //TODO 这里是从设备控制页跳转到列表页，暂定为finish本页
            baractivity.finish();
            ActivitiesManagerApplication ama = new ActivitiesManagerApplication();
            ama.addDestoryActivity(baractivity, ConstPara.HOME_PAGE);
            baractivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    public ImageView getRightToolbar() {
        return toolbar_right_img;
    }
}
