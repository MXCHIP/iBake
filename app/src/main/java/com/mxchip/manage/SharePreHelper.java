package com.mxchip.manage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Rocke on 2016/03/09.
 */
public class SharePreHelper {

    private String TAG = "---SharePreHelper---";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharePreHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("fogcloud", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * username
     * enduserid
     * devicepw
     * token
     *
     * @param key
     * @param value
     */
    public void addData(String key, String value) {

        Log.d(TAG, "addData -- " + key + "" + value);

        // 将用户登录成功后获取的token放入local storege里
        editor.putString(key, value);
        // 提交当前数据
        editor.commit();
    }

    public String getData(String key) {
        Log.d(TAG, "getData -- " + key);
        return sharedPreferences.getString(key, "");
    }

    public void removeData(String key) {
        Log.d(TAG, "removeData -- " + key);
        editor.remove(key);
        editor.commit();
    }

}
