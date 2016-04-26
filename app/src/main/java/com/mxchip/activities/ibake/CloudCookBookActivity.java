package com.mxchip.activities.ibake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

/**
 * Created by Rocke on 2016/03/10.
 */
public class CloudCookBookActivity extends AppCompatActivity {
    private String TAG = "---AddDevNextActivity---";
    private SetTitleBar stb;
    private EditText cloud_cb_search;
    private ImageView cloud_cb_cleanbtn;
    private TextView cloud_cb_search_btn;

    //子按钮
    //bread
    private LinearLayout cloud_cb_breadly;
    private ImageView cloud_cb_breadimg;
    private TextView cloud_cb_breadtxt;
    //cake
    private LinearLayout cloud_cb_cakely;
    private ImageView cloud_cb_cakeimg;
    private TextView cloud_cb_caketxt;
    //biscuit
    private LinearLayout cloud_cb_biscuitly;
    private ImageView cloud_cb_biscuitimg;
    private TextView cloud_cb_biscuittxt;
    //pizza
    private LinearLayout cloud_cb_pizzaly;
    private ImageView cloud_cb_pizzaimg;
    private TextView cloud_cb_pizzatxt;
    //fish
    private LinearLayout cloud_cb_fishly;
    private ImageView cloud_cb_fishimg;
    private TextView cloud_cb_fishtxt;
    //meat
    private LinearLayout cloud_cb_meatly;
    private ImageView cloud_cb_meatimg;
    private TextView cloud_cb_meattxt;
    //chicken
    private LinearLayout cloud_cb_chickenly;
    private ImageView cloud_cb_chickenimg;
    private TextView cloud_cb_chickentxt;
    //egg tant
    private LinearLayout cloud_cb_eggly;
    private ImageView cloud_cb_eggimg;
    private TextView cloud_cb_eggtxt;
    //muffin
    private LinearLayout cloud_cb_muffinly;
    private ImageView cloud_cb_muffinimg;
    private TextView cloud_cb_muffintxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_cookbook);

        stb = new SetTitleBar(CloudCookBookActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_RECIPE);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        initView();
        initOnClick();
    }

    //    初始化view
    private void initView() {
        cloud_cb_search = (EditText) findViewById(R.id.cloud_cb_search_txt);
        cloud_cb_cleanbtn = (ImageView) findViewById(R.id.cloud_cb_cleanbtn);
        cloud_cb_search_btn = (TextView) findViewById(R.id.cloud_cb_search_btn);

        //子按钮的初始化
        //bread
        cloud_cb_breadly = (LinearLayout) findViewById(R.id.cloud_cb_breadly);
        cloud_cb_breadimg = (ImageView) findViewById(R.id.cloud_cb_breadimg);
        cloud_cb_breadtxt = (TextView) findViewById(R.id.cloud_cb_breadtxt);
        //cake
        cloud_cb_cakely = (LinearLayout) findViewById(R.id.cloud_cb_cakely);
        cloud_cb_cakeimg = (ImageView) findViewById(R.id.cloud_cb_cakeimg);
        cloud_cb_caketxt = (TextView) findViewById(R.id.cloud_cb_caketxt);
        //biscuit
        cloud_cb_biscuitly = (LinearLayout) findViewById(R.id.cloud_cb_biscuitly);
        cloud_cb_biscuitimg = (ImageView) findViewById(R.id.cloud_cb_biscuitimg);
        cloud_cb_biscuittxt = (TextView) findViewById(R.id.cloud_cb_biscuittxt);
        //pizza
        cloud_cb_pizzaly = (LinearLayout) findViewById(R.id.cloud_cb_pizzaly);
        cloud_cb_pizzaimg = (ImageView) findViewById(R.id.cloud_cb_pizzaimg);
        cloud_cb_pizzatxt = (TextView) findViewById(R.id.cloud_cb_pizzatxt);
        //fish
        cloud_cb_fishly = (LinearLayout) findViewById(R.id.cloud_cb_fishly);
        cloud_cb_fishimg = (ImageView) findViewById(R.id.cloud_cb_fishimg);
        cloud_cb_fishtxt = (TextView) findViewById(R.id.cloud_cb_fishtxt);
        //meat
        cloud_cb_meatly = (LinearLayout) findViewById(R.id.cloud_cb_meatly);
        cloud_cb_meatimg = (ImageView) findViewById(R.id.cloud_cb_meatimg);
        cloud_cb_meattxt = (TextView) findViewById(R.id.cloud_cb_meattxt);
        //chicken
        cloud_cb_chickenly = (LinearLayout) findViewById(R.id.cloud_cb_chickenly);
        cloud_cb_chickenimg = (ImageView) findViewById(R.id.cloud_cb_chickenimg);
        cloud_cb_chickentxt = (TextView) findViewById(R.id.cloud_cb_chickentxt);
        //egg tant
        cloud_cb_eggly = (LinearLayout) findViewById(R.id.cloud_cb_eggly);
        cloud_cb_eggimg = (ImageView) findViewById(R.id.cloud_cb_eggimg);
        cloud_cb_eggtxt = (TextView) findViewById(R.id.cloud_cb_eggtxt);
        //muffin
        cloud_cb_muffinly = (LinearLayout) findViewById(R.id.cloud_cb_muffinly);
        cloud_cb_muffinimg = (ImageView) findViewById(R.id.cloud_cb_muffinimg);
        cloud_cb_muffintxt = (TextView) findViewById(R.id.cloud_cb_muffintxt);
    }

    //    初始化click事件
    private void initOnClick() {
        cloud_cb_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cloud_cb_search_btn.setVisibility(View.VISIBLE);
                } else {
                    cloud_cb_cleanbtn.setVisibility(View.GONE);
                    cloud_cb_search_btn.setVisibility(View.GONE);
                }
            }
        });

        cloud_cb_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cookbookname = cloud_cb_search.getText().toString().trim();
                SharePreHelper shareph = new SharePreHelper(CloudCookBookActivity.this);
                if (ConstHelper.checkPara(cookbookname)) {

                    MiCOUser miCOUser = new MiCOUser();
                    miCOUser.searchCookBookByName(cookbookname, new UserCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d(TAG, message);
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            Log.d(TAG, code + "msg = " + message);
                        }
                    },shareph.getData(ConstPara.SHARE_TOKEN));
                }
            }
        });


        cloud_cb_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    cloud_cb_cleanbtn.setVisibility(View.VISIBLE);
                else
                    cloud_cb_cleanbtn.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cloud_cb_cleanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cloud_cb_search.setText("");
            }
        });

        //选择事件
        //bread
        cloud_cb_breadly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_BREAD, cloud_cb_breadly, cloud_cb_breadimg, cloud_cb_breadtxt);
            }
        });
        //cake
        cloud_cb_cakely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_CAKE, cloud_cb_cakely, cloud_cb_cakeimg, cloud_cb_caketxt);
            }
        });
        //biscuit
        cloud_cb_biscuitly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_BISCUIT, cloud_cb_biscuitly, cloud_cb_biscuitimg, cloud_cb_biscuittxt);
            }
        });
        //pizza
        cloud_cb_pizzaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_PIZZA, cloud_cb_pizzaly, cloud_cb_pizzaimg, cloud_cb_pizzatxt);
            }
        });
        //fish
        cloud_cb_fishly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_FISH, cloud_cb_fishly, cloud_cb_fishimg, cloud_cb_fishtxt);
            }
        });
        //meat
        cloud_cb_meatly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_MEAT, cloud_cb_meatly, cloud_cb_meatimg, cloud_cb_meattxt);
            }
        });
        //chicken
        cloud_cb_chickenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_CHICKEN, cloud_cb_chickenly, cloud_cb_chickenimg, cloud_cb_chickentxt);
            }
        });
        //egg
        cloud_cb_eggly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_EGGTAN, cloud_cb_eggly, cloud_cb_eggimg, cloud_cb_eggtxt);
            }
        });
        //muffin
        cloud_cb_muffinly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe(ConstPara.TITLENAME_MUFFIN, cloud_cb_muffinly, cloud_cb_muffinimg, cloud_cb_muffintxt);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int isvisib = cloud_cb_cleanbtn.getVisibility();
            if ((0 == isvisib)) {
                cloud_cb_search.clearFocus();
                cloud_cb_search.setText("");
            } else {
                finish();
            }
        }
        return true;
    }

    private void switchRecipe(String name, LinearLayout layout, ImageView imageView, TextView textView) {

        //将TAG的UI置为初始化
        initTagView();

        //将选中的变色
        layout.setBackgroundColor(ConstPara.MAIN_COLOR);
        textView.setTextColor(ConstPara.RECIP_TXT_COLOR);

        int type = ConstPara.RECIP_TYPE_BREAD;

        switch (name) {
            case ConstPara.TITLENAME_BREAD:
                imageView.setImageResource(R.drawable.recipe_btn_bread_s);
                type = ConstPara.RECIP_TYPE_BREAD;
                break;
            case ConstPara.TITLENAME_CAKE:
                imageView.setImageResource(R.drawable.recipe_btn_cake_s);
                type = ConstPara.RECIP_TYPE_CAKE;
                break;
            case ConstPara.TITLENAME_BISCUIT:
                imageView.setImageResource(R.drawable.recipe_btn_biscuit_s);
                type = ConstPara.RECIP_TYPE_BISCUIT;
                break;
            case ConstPara.TITLENAME_PIZZA:
                imageView.setImageResource(R.drawable.recipe_btn_pizza_s);
                type = ConstPara.RECIP_TYPE_PIZZA;
                break;
            case ConstPara.TITLENAME_FISH:
                imageView.setImageResource(R.drawable.recipe_btn_fish_s);
                type = ConstPara.RECIP_TYPE_FISH;
                break;
            case ConstPara.TITLENAME_MEAT:
                imageView.setImageResource(R.drawable.recipe_btn_meat_s);
                type = ConstPara.RECIP_TYPE_MEAT;
                break;
            case ConstPara.TITLENAME_CHICKEN:
                imageView.setImageResource(R.drawable.recipe_btn_chicken_s);
                type = ConstPara.RECIP_TYPE_CHICKEN;
                break;
            case ConstPara.TITLENAME_EGGTAN:
                imageView.setImageResource(R.drawable.recipe_btn_egg_tant_s);
                type = ConstPara.RECIP_TYPE_EGG;
                break;
            case ConstPara.TITLENAME_MUFFIN:
                imageView.setImageResource(R.drawable.recipe_btn_muffin_s);
                type = ConstPara.RECIP_TYPE_MUFFIN;
                break;
        }

        //重新赋值
        TAG_NAME = name;TAG_LAYOUT = layout;TAG_IMG = imageView;TAG_TEXT = textView;

        //跳转页面
        Intent intent = new Intent(CloudCookBookActivity.this, RecipesActivity.class);
        intent.putExtra(ConstPara.INTENT_RECIPENAME, name);
        intent.putExtra(ConstPara.INTENT_RECIPETYPE, type);
        startActivity(intent);
    }

    //名字标签
    String TAG_NAME = "";
    //layout标签
    LinearLayout TAG_LAYOUT;
    //图片标签
    ImageView TAG_IMG;
    //文本标签
    TextView TAG_TEXT;

    private void initTagView(){

        if(!ConstHelper.checkPara(TAG_NAME))
            return;

        TAG_LAYOUT.setBackgroundResource(R.drawable.full_box);
        TAG_TEXT.setTextColor(Color.parseColor("#FCCB0F"));
        switch (TAG_NAME) {
            case ConstPara.TITLENAME_BREAD:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_bread_n);
                break;
            case ConstPara.TITLENAME_CAKE:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_cake_n);
                break;
            case ConstPara.TITLENAME_BISCUIT:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_biscuit_n);
                break;
            case ConstPara.TITLENAME_PIZZA:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_pizza_n);
                break;
            case ConstPara.TITLENAME_FISH:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_fish_n);
                break;
            case ConstPara.TITLENAME_MEAT:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_meat_n);
                break;
            case ConstPara.TITLENAME_CHICKEN:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_chicken_n);
                break;
            case ConstPara.TITLENAME_EGGTAN:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_egg_tant_n);
                break;
            case ConstPara.TITLENAME_MUFFIN:
                TAG_IMG.setImageResource(R.drawable.recipe_btn_muffin_n);
                break;
        }
    }
}
