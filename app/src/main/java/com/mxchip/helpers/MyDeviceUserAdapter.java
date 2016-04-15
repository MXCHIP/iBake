package com.mxchip.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mico.micosdk.MiCOUser;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.manage.ActionSheetDialog;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.SharePreHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by Rocke on 2016/03/07.
 */
public class MyDeviceUserAdapter extends BaseAdapter {
    private class buttonViewHolder {
        TextView mydev_user_txid;
        TextView mydev_user_phone_txid;
        ImageView mydev_user_rm_imgid;
    }

    private final String TAG = "---MyDeviceItmAdapter---";
    private List<Map<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private int golresource;
    private buttonViewHolder holder;

    private String deviceid;

    public MyDeviceUserAdapter(Context c, List<Map<String, Object>> appList, int resource,
                               String[] from, int[] to, String extra) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
        golresource = resource;

        deviceid = extra;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(golresource, null);
        Map<String, Object> appInfo = mAppList.get(position);
        holder = new buttonViewHolder();
        holder.mydev_user_txid = (TextView) convertView.findViewById(valueViewID[0]);
        holder.mydev_user_phone_txid = (TextView) convertView.findViewById(valueViewID[1]);
        holder.mydev_user_rm_imgid = (ImageView) convertView.findViewById(valueViewID[2]);

        holder.mydev_user_txid.setText((String) appInfo.get(keyString[0]));
        holder.mydev_user_phone_txid.setText((String) appInfo.get(keyString[1]));
        holder.mydev_user_rm_imgid.setOnClickListener(new userButtonListener(this, convertView.getContext(), deviceid, (String) appInfo.get(keyString[2])));
        return convertView;
    }
}

class userButtonListener implements View.OnClickListener {
    private BaseAdapter mba;;
    private Context mcontext;
    private String mdeviceid;
    private String menduserid;

    userButtonListener(BaseAdapter ba,Context context, String deviceid, String enduserid) {
        mba = ba;
        mcontext = context;
        mdeviceid = deviceid;
        menduserid = enduserid;
    }

    @Override
    public void onClick(View v) {
        Log.d("---userButton---", "Remove mdeviceid = " + menduserid);
        // TODO 移除某人的控制权限
        final Context context = v.getContext();
        new ActionSheetDialog(context)
                .builder()
                .setTitle("Remove the user?")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("Yes", ActionSheetDialog.SheetItemColor.Yellow,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Log.d("---My---", "removeBindRole");
                                removeBindRole(context);
                            }
                        }).show();

    }

    private void removeBindRole(final Context context){
        MiCOUser micoUser = new MiCOUser();
        String token = new SharePreHelper(mcontext).getData("token");
//        Log.d("---My---", mdeviceid + " " + menduserid + " " + token);

        micoUser.removeBindRole(mdeviceid, menduserid, new UserCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.d("---My---", message);
                mba.notifyDataSetChanged();
                ConstHelper.setToast(context, ConstHelper.getFogMessage(message));
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d("---My---", message);
            }
        },token);
    }
}