package com.mxchip.manage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.TradeConfigs;
import com.alibaba.sdk.android.trade.TradeConstants;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.page.ItemDetailPage;
import com.taobao.tae.sdk.callback.InitResultCallback;
import com.taobao.tae.sdk.model.TaokeParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rocke on 2016/03/09.
 * 动画效果
 * 注意，切换方法overridePendingTransition只能在startActivity和finish方法之后调用。
 * 第一个参数为第一个Activity离开时的动画，第二参数为所进入的Activity的动画效果
 * 有的是安卓自带，在前面添加android.
 * <p/>
 * 淡入淡出效果
 * overridePendingTransition(R.anim.fade, R.anim.hold);
 * 放大淡出效果
 * overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);
 * 转动淡出效果
 * overridePendingTransition(R.anim.scale_rotate,R.anim.my_alpha_action);
 * 转动淡出效果
 * overridePendingTransition(R.anim.scale_translate_rotate,R.anim.my_alpha_action);
 * 左上角展开淡出效果
 * overridePendingTransition(R.anim.scale_translate,R.anim.my_alpha_action);
 * 压缩变小淡出效果
 * overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);
 * 右往左推出效果
 * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
 * 下往上推出效果
 * overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
 * 左右交错效果
 * overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
 * 放大淡出效果
 * overridePendingTransition(R.anim.wave_scale,R.anim.my_alpha_action);
 * 缩小效果
 * overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
 * 上下交错效果
 * overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
 * <p/>
 * overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
 */
public class ConstHelper {
    private static String TAG = "---ConstHelper---";

    /**
     * 判断是否为空
     *
     * @param param
     * @return
     */
    public static boolean checkPara(String... param) {
        if (null == param || param.equals("")) {
            return false;
        } else if (param.length > 0) {
            for (String str : param) {
                if (null == str || str.equals("") || str.equals("null") || str.equals("undefined")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * toast 一些信息
     *
     * @param context
     * @param message
     */
    public static void setToast(Context context, String message) {
        if (checkPara(message) && (null != context)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFogMessage(String message) {
        if (message.indexOf("meta") > -1) {
            if (message.indexOf("message") > -1) {
                try {
                    JSONObject metajson = new JSONObject(message);
                    JSONObject msgjson = new JSONObject(metajson.getString("meta"));
                    return msgjson.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getFogErr(String message) {
        if (message.indexOf("non_field_errors") > -1) {
            try {
                JSONObject loginjson = new JSONObject(message);
                return loginjson.getString("non_field_errors");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFogToken(String message) {
        if (message.indexOf("token") > -1) {
            try {
                JSONObject loginjson = new JSONObject(message);
                return loginjson.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFogEndUserid(String message) {
        if (message.indexOf("clientid") > -1) {
            try {
                JSONObject loginjson = new JSONObject(message);
                return loginjson.getString("clientid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFogCode(String message) {
        if (message.indexOf("meta") > -1) {
            if (message.indexOf("code") > -1) {
                try {
                    JSONObject metajson = new JSONObject(message);
                    JSONObject msgjson = new JSONObject(metajson.getString("meta"));
                    return msgjson.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getFogData(String message) {
        if (message.indexOf("meta") > -1) {
            if (message.indexOf("data") > -1) {
                try {
                    JSONObject metajson = new JSONObject(message);
                    return metajson.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    //图片灰化处理
    public static Bitmap getGrayBitmap(Bitmap mBitmap) {
        Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mGrayBitmap);
        Paint mPaint = new Paint();

        //创建颜色变换矩阵
        ColorMatrix mColorMatrix = new ColorMatrix();
        //设置灰度影响范围
        mColorMatrix.setSaturation(0);
        //创建颜色过滤矩阵
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        //设置画笔的颜色过滤矩阵
        mPaint.setColorFilter(mColorFilter);
        //使用处理后的画笔绘制图像
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);

        return mGrayBitmap;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
//        Bitmap bitmap = Bitmap
//                .createBitmap(
//                        drawable.getIntrinsicWidth(),
//                        drawable.getIntrinsicHeight(),
//                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                : Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight());
//        drawable.draw(canvas);

        BitmapDrawable bd = (BitmapDrawable) drawable;

        return bd.getBitmap();
    }

    // Bitmap转换成byte[]
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    // Bitmap转换成Drawable
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    public static JSONObject getPayload(String message) {
        try {
            JSONObject jsonobj = new JSONObject(message);
            return jsonobj.getJSONObject("payload");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonValue(String message) {
        try {
            JSONObject jsonobj = new JSONObject(message);
            return jsonobj.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //初始化阿里百川的SDK
    public static void initAlibabaSDK(final Context context) {
        TradeConfigs.defaultTaokePid = ConstPara._MMPID;
        AlibabaSDK.asyncInit(context, new InitResultCallback() {
            @Override
            public void onSuccess() {
                setToast(context, "TaeSDK 初始化成功");
            }

            @Override
            public void onFailure(int code, String message) {
                Log.w(TAG, "初始化异常，code = " + code + ", info = " + message);
            }
        });
    }

    //通过itemid打开淘宝的宝贝详情界面
    public static void showItemDetailPage(Activity context, String isvcode, String itemid) {
        TradeService tradeService = AlibabaSDK.getService(TradeService.class);
        Map<String, String> exParams = new HashMap<String, String>();

        if(!ConstHelper.checkPara(isvcode))
            isvcode = ConstPara._ISVCODE;

        exParams.put(TradeConstants.ISV_CODE, isvcode);
        exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.TAOBAO_NATIVE_VIEW);
        ItemDetailPage itemDetailPage = new ItemDetailPage(itemid, exParams);
        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = ConstPara._MMPID;
        tradeService.show(itemDetailPage, taokeParams, context, null, new com.alibaba.sdk.android.trade.callback.TradeProcessCallback() {
            @Override
            public void onPaySuccess(com.alibaba.sdk.android.trade.model.TradeResult tradeResult) {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}