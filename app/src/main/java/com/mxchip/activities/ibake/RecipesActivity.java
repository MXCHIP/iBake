package com.mxchip.activities.ibake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.helpers.CookBookItemAdapter;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;
import com.mxchip.manage.SetTitleBar;
import com.mxchip.manage.SharePreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rocke on 2016/03/28.
 */
public class RecipesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String TAG = "---RecipesActivity---";

    private ListView recipes_list;
    private CookBookItemAdapter adapter;
    private SetTitleBar stb;
    private String reqtype;
    private String recipename;
    private MiCOUser micoUser;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_list);

        reqtype = (String) getIntent().getSerializableExtra(ConstPara.INTENT_REQTYPE);
        recipename = (String) getIntent().getSerializableExtra(ConstPara.INTENT_RECIPENAME);

        stb = new SetTitleBar(RecipesActivity.this);
        if (ConstHelper.checkPara(recipename)) {
            stb.setTitleName(recipename);
        }
        stb.setLeftButton("back", "finish");
        stb.setRightButton("none", "");

        micoUser = new MiCOUser();
        SharePreHelper shareph = new SharePreHelper(RecipesActivity.this);
        token = shareph.getData(ConstPara.SHARE_TOKEN);

        initView();
    }

    private void initView() {
        recipes_list = (ListView) findViewById(R.id.recipes_list);

        initDevList();
    }

    /**
     * 通过type来获取食谱列表
     */
    private void cookBookListBytype() {
        int type = getIntent().getIntExtra(ConstPara.INTENT_RECIPETYPE, 1);
        String productid = ConstPara._PRODUCTID;

        micoUser.getCookBookList(type, productid, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG + "onSuccess", message);
                loadDate(message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG + "onFailure", code + " " + message);
            }
        }, token);
    }

    private void getCBListByname(){
        if (ConstHelper.checkPara(recipename)) {

            MiCOUser miCOUser = new MiCOUser();
            miCOUser.searchCookBookByName(recipename, new UserCallBack() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, message);
                    loadDate(message);
                }

                @Override
                public void onFailure(int code, String message) {
                    Log.d(TAG, code + "msg = " + message);
                }
            }, token);
        }
    }


    private void initDevList() {
        adapter = new CookBookItemAdapter(RecipesActivity.this, recipes_list);
        recipes_list.setOnItemClickListener(this);

        if(reqtype.equals(ConstPara.INTENT_REQTYPE_TYPE))
            cookBookListBytype();
        else if(reqtype.equals(ConstPara.INTENT_REQTYPE_NAME))
            getCBListByname();
    }

    public void loadDate(String data) {

        try {
            JSONArray itemsArr = new JSONArray(ConstHelper.getFogData(data));
            for (int i = 0; i < itemsArr.length(); i++) {
                final JSONObject items  = itemsArr.getJSONObject(i);

                String cb_name = items.getString("name");
                String cb_img = items.getString("mainimageurl");
                String cb_likeno = items.getString("like_count");
                String cb_recipeid = items.getString("id");

                adapter.addBook(cb_name, cb_img, cb_likeno, cb_recipeid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipes_list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
