package com.mxchip.manage;

import java.util.List;

/**
 * Created by Rocke on 2016/04/12.
 */
public class TempMap {

    public static int getIndexFromTempMap(String key, List<String> list){

        for(int i = 0; i<list.size();i++){
            if(key.equals(list.get(i))){
                return  i;
            }
        }
        return 0;
    }
}
