package com.mxchip.activities.ibake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.SetTitleBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        String recipeid = (String) getIntent().getSerializableExtra("recipeid");
        String recipename = (String) getIntent().getSerializableExtra("recipename");
        stb = new SetTitleBar(RecipeDetailActivity.this);
        stb.setTitleName(recipename);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("edit", "edit");


        initMaterial();
        addLinearLayout();
    }

    //    获取一共需要哪些食材
    private void initMaterial() {
        recipe_dd_material_lv = (ListView) findViewById(R.id.recipe_dd_material_lv);

        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.recipe_dd_material_item,
                new String[]{"name", "kg"},
                new int[]{R.id.recipe_dd_buttertv, R.id.recipe_dd_butterkgtv});
        recipe_dd_material_lv.setAdapter(adapter);
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
}
