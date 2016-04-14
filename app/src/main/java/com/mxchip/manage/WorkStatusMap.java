package com.mxchip.manage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rocke on 2016/04/12.
 */
public class WorkStatusMap {
    Map<String, String> map = new HashMap<String, String>();

    /**
     * WorkStatus
     */
    public String DJ_CODE = "0";
    String DJ_NAME = "waiting";
    String YY_CODE = "1";
    String YY_NAME = "waiting";
    String HK_CODE = "2";
    String HK_NAME = "waiting";
    String ZT_CODE = "3";
    String ZT_NAME = "waiting";
    String YR_CODE = "4";
    String YR_NAME = "waiting";
    String WC_CODE = "5";
    String WC_NAME = "waiting";
    String YRWC_CODE = "6";
    String YRWC_NAME = "waiting";

    public WorkStatusMap() {
        map.put(DJ_CODE, DJ_NAME);
        map.put(YY_CODE, YY_NAME);
        map.put(HK_CODE, HK_NAME);
        map.put(ZT_CODE, ZT_NAME);
        map.put(YR_CODE, YR_NAME);
        map.put(WC_CODE, WC_NAME);
        map.put(YRWC_CODE, YRWC_NAME);
    }

    public String getStatusName(String key){

        return map.get(key);
    }

}
