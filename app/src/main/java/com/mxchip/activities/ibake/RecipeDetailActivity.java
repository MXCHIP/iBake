package com.mxchip.activities.ibake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rocke on 2016/03/29.
 */
public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "---RecipeDetailActivity---";
    private SetTitleBar stb;

    private ListView recipe_dd_material_lv;
    private LinearLayout recipe_detial_showtaobao;
    private LinearLayout recipe_detial_ilikeit;

    private String itemid = ConstPara._ITEMID;

    MiCOUser micouser;
    int recipeid;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        String recipename = (String) getIntent().getSerializableExtra(ConstPara.INTENT_RECIPENAME);
        stb = new SetTitleBar(RecipeDetailActivity.this);
        stb.setTitleName(recipename);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("edit", "edit");


        micouser = new MiCOUser();
        recipeid = Integer.parseInt((String) getIntent().getSerializableExtra(ConstPara.INTENT_RECIPEID));
        SharePreHelper shareph = new SharePreHelper(RecipeDetailActivity.this);
        token = shareph.getData(ConstPara.SHARE_TOKEN);

        initMaterial();
        addLinearLayout();
        initOnClick();
        getThisBookInfo();

    }

    //    获取一共需要哪些食材
    private void initMaterial() {
        recipe_dd_material_lv = (ListView) findViewById(R.id.recipe_dd_material_lv);
        recipe_detial_showtaobao = (LinearLayout) findViewById(R.id.recipe_detial_showtaobao);
        recipe_detial_ilikeit = (LinearLayout) findViewById(R.id.recipe_detial_ilikeit);

        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.recipe_dd_material_item,
                new String[]{"name", "kg"},
                new int[]{R.id.recipe_dd_buttertv, R.id.recipe_dd_butterkgtv});
        recipe_dd_material_lv.setAdapter(adapter);
    }

    private void initOnClick() {
        ConstHelper.initAlibabaSDK(RecipeDetailActivity.this);

        //打开淘宝的界面
        recipe_detial_showtaobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstHelper.showItemDetailPage(RecipeDetailActivity.this, null, itemid);
            }
        });

        //点个赞
        recipe_detial_ilikeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                micouser.addCookBookLikeNo(recipeid, new UserCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.d(TAG, code + "msg = " + message);
                    }
                }, token);
            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        ViewGroup.LayoutParams params = recipe_dd_material_lv.getLayoutParams();

        int i = 1;
        for (; i < 11; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "butter " + i);
            map.put("kg", 100 + i);
            list.add(map);
        }
        //动态设置listview的高度
        params.height = ConstHelper.dip2px(RecipeDetailActivity.this, 20 * (i - 1));
        recipe_dd_material_lv.setLayoutParams(params);

        return list;
    }

    private void addLinearLayout() {
        LinearLayout recipe_dd_more_lys = (LinearLayout) findViewById(R.id.recipe_dd_more_lys);
        View viewOne1 = getLayoutInflater().inflate(R.layout.recipe_bake_steps, null);
        View viewOne2 = getLayoutInflater().inflate(R.layout.recipe_bake_steps, null);
        recipe_dd_more_lys.removeAllViews();
        recipe_dd_more_lys.addView(viewOne1, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        recipe_dd_more_lys.addView(viewOne2, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        addNowMaterial(viewOne1);
        addNowMaterial(viewOne2);
    }

    private void addNowMaterial(View view) {
        ListView nowlv = (ListView) view.findViewById(R.id.recipe_bake_step_material_lv);

        SimpleAdapter adapter = new SimpleAdapter(this, geOnetData(nowlv), R.layout.recipe_dd_material_item,
                new String[]{"name", "kg"},
                new int[]{R.id.recipe_dd_buttertv, R.id.recipe_dd_butterkgtv});
        nowlv.setAdapter(adapter);
    }

    private List<Map<String, Object>> geOnetData(ListView nowlv) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        ViewGroup.LayoutParams params = nowlv.getLayoutParams();

        int i = 1;
        for (; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "oil " + i);
            map.put("kg", 100 + i);
            list.add(map);
        }
        //动态设置listview的高度
        params.height = ConstHelper.dip2px(RecipeDetailActivity.this, 20 * (i - 1));
        nowlv.setLayoutParams(params);

        return list;
    }

    private void getThisBookInfo() {
        micouser.getCookBookInfo(recipeid, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, code + "msg = " + message);
            }
        }, token);
    }
}
