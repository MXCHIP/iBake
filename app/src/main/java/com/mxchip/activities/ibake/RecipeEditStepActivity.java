package com.mxchip.activities.ibake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;

/**
 * Created by Rocke on 2016/04/01.
 */
public class RecipeEditStepActivity extends AppCompatActivity {
    private static final String TAG = "---RecipeEditStep---";
    private SetTitleBar stb;

    private LinearLayout recipe_ed_imgly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit_step);

        // String recipename = (String) getIntent().getSerializableExtra("recipename");
        stb = new SetTitleBar(RecipeEditStepActivity.this);
        stb.setTitleName(ConstPara.TITLENAME_STEP);
        stb.setLeftButton("back", "finish");
        stb.setRightButton("save", "save");

        initView();
        initOnClick();
    }

    private void initView(){

    }

    private void initOnClick(){

    }
}
