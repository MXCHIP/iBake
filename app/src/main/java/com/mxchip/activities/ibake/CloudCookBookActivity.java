package com.mxchip.activities.ibake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;

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
                switchRecipe("bread", cloud_cb_breadly, cloud_cb_breadimg, cloud_cb_breadtxt);
            }
        });
        //cake
        cloud_cb_cakely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("cake", cloud_cb_cakely, cloud_cb_cakeimg, cloud_cb_caketxt);
            }
        });
        //biscuit
        cloud_cb_biscuitly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("biscuit", cloud_cb_biscuitly, cloud_cb_biscuitimg, cloud_cb_biscuittxt);
            }
        });
        //pizza
        cloud_cb_pizzaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("pizza", cloud_cb_pizzaly, cloud_cb_pizzaimg, cloud_cb_pizzatxt);
            }
        });
        //fish
        cloud_cb_fishly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("fish", cloud_cb_fishly, cloud_cb_fishimg, cloud_cb_fishtxt);
            }
        });
        //meat
        cloud_cb_meatly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("meat", cloud_cb_meatly, cloud_cb_meatimg, cloud_cb_meattxt);
            }
        });
        //chicken
        cloud_cb_chickenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("chicken", cloud_cb_chickenly, cloud_cb_chickenimg, cloud_cb_chickentxt);
            }
        });
        //egg
        cloud_cb_eggly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("egg", cloud_cb_eggly, cloud_cb_eggimg, cloud_cb_eggtxt);
            }
        });
        //muffin
        cloud_cb_muffinly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRecipe("muffin", cloud_cb_muffinly, cloud_cb_muffinimg, cloud_cb_muffintxt);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // if the imgview is gone getvisibility() is 0
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

    private void switchRecipe(String type, LinearLayout layout, ImageView imageView, TextView textView) {

        //将TAG的UI置为初始化
        initTagView();

        //将选中的变色
        layout.setBackgroundColor(Color.parseColor("#FCCB0F"));
        textView.setTextColor(Color.parseColor("#655106"));
        switch (type) {
            case "bread":
                imageView.setImageResource(R.drawable.recipe_btn_bread_s);
                break;
            case "cake":
                imageView.setImageResource(R.drawable.recipe_btn_cake_s);
                break;
            case "biscuit":
                imageView.setImageResource(R.drawable.recipe_btn_biscuit_s);
                break;
            case "pizza":
                imageView.setImageResource(R.drawable.recipe_btn_pizza_s);
                break;
            case "fish":
                imageView.setImageResource(R.drawable.recipe_btn_fish_s);
                break;
            case "meat":
                imageView.setImageResource(R.drawable.recipe_btn_meat_s);
                break;
            case "chicken":
                imageView.setImageResource(R.drawable.recipe_btn_chicken_s);
                break;
            case "egg":
                imageView.setImageResource(R.drawable.recipe_btn_egg_tant_s);
                break;
            case "muffin":
                imageView.setImageResource(R.drawable.recipe_btn_muffin_s);
                break;
        }

        //重新赋值
        TAG_TYPE = type;TAG_LAYOUT = layout;TAG_IMG = imageView;TAG_TEXT = textView;

        //跳转页面
        Intent intent = new Intent(CloudCookBookActivity.this, RecipesActivity.class);
        intent.putExtra("recipename", ConstPara.TITLENAME_CAKE);
        startActivity(intent);
    }

    //名字标签
    String TAG_TYPE = "";
    //layout标签
    LinearLayout TAG_LAYOUT;
    //图片标签
    ImageView TAG_IMG;
    //文本标签
    TextView TAG_TEXT;

    private void initTagView(){

        if(!ConstHelper.checkPara(TAG_TYPE))
            return;

        TAG_LAYOUT.setBackgroundResource(R.drawable.full_box);
        TAG_TEXT.setTextColor(Color.parseColor("#FCCB0F"));
        switch (TAG_TYPE) {
            case "bread":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_bread_n);
                break;
            case "cake":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_cake_n);
                break;
            case "biscuit":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_biscuit_n);
                break;
            case "pizza":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_pizza_n);
                break;
            case "fish":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_fish_n);
                break;
            case "meat":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_meat_n);
                break;
            case "chicken":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_chicken_n);
                break;
            case "egg":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_egg_tant_n);
                break;
            case "muffin":
                TAG_IMG.setImageResource(R.drawable.recipe_btn_muffin_n);
                break;
        }
    }
}
