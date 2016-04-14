package com.mxchip.helpers;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Rocke on 2016/03/16.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        // Auto-generated method stub
        super.onCreate();
        File f = new File(Environment.getExternalStorageDirectory()+"/iBakImgs/");
        if(!f.exists()){
            f.mkdir();
        }
    }

}
