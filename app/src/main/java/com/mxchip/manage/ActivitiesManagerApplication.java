package com.mxchip.manage;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rocke on 2016/03/04.
 */
public class ActivitiesManagerApplication extends Application {
    private static Map<String, Activity> destoryMap = new HashMap<>();


    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            if (activityName.equals(key))
                destoryMap.get(key).finish();
        }
    }
}
